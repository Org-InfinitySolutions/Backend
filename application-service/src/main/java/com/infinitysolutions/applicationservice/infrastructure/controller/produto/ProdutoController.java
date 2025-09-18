package com.infinitysolutions.applicationservice.infrastructure.controller.produto;

import com.infinitysolutions.applicationservice.core.domain.produto.Produto;
import com.infinitysolutions.applicationservice.core.usecases.produto.*;
import com.infinitysolutions.applicationservice.infrastructure.mapper.produto.ProdutoEntityMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.ProdutoAtualizacaoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.ProdutoCriacaoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.ProdutoRespostaDTO;
import com.infinitysolutions.applicationservice.old.infra.utils.AuthenticationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@Validated
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Endpoints com funções que envolvem os produtos do sistema em geral")
public class ProdutoController {

    private final AuthenticationUtils authenticationUtils;
    private final AtualizarImagemProduto atualizarImagemProdutoCase;
    private final CriarProduto criarProdutoCase;
    private final ListarTodosProdutos listarTodosProdutos;
    private final BuscarProdutoPorId buscarProdutoPorId;
    private final AtualizarProduto atualizarProduto;
    private final ExcluirProduto excluirProduto;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Listar todos os produtos",
            description = "Retorna uma lista com produtos cadastrados no sistema. " +
                         "Usuários não autenticados, pessoas físicas e jurídicas veem apenas produtos ativos. " +
                         "Funcionários e administradores veem todos os produtos, incluindo inativos."
    )
    public List<ProdutoRespostaDTO> listarTodosProdutos(
            @Parameter(description = "Informações de autenticação do usuário", hidden = true)
            Authentication authentication) {
        return listarTodosProdutos.execute(authenticationUtils.isAdminOrEmployee(authentication)).stream().map(ProdutoEntityMapper::toProdutoGenericoRespostaDTO).toList();
    }
      @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Buscar produto por ID",
            description = "Retorna os detalhes completos de um produto específico a partir do seu ID. " +
                         "Usuários não autenticados, pessoas físicas e jurídicas veem apenas produtos ativos. " +
                         "Funcionários e administradores veem todos os produtos, incluindo inativos."
    )
    public ProdutoRespostaDTO buscarProdutoPorId(
            @Parameter(description = "ID do produto a ser buscado", required = true)
            @PathVariable @Positive Integer id,
            @Parameter(description = "Informações de autenticação do usuário", hidden = true)
            Authentication authentication) {

          Produto produto = buscarProdutoPorId.execute(authenticationUtils.isAdminOrEmployee(authentication), id);
          return ProdutoEntityMapper.toProdutoGenericoRespostaDTO(produto);
      }
      @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Criar um novo produto",
            description = "Cria um novo produto no sistema a partir dos dados fornecidos. " +
                         "Acesso restrito apenas para administradores e funcionários."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    encoding = {
                            @Encoding(name = "produto", contentType = MediaType.APPLICATION_JSON_VALUE),
                            @Encoding(name = "imagem", contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    }
            )
    )
    public ProdutoRespostaDTO criarProduto(
            @Parameter(description = "Dados do produto em JSON", required = true)
            @RequestPart("produto") @Valid ProdutoCriacaoDTO dto,
            @Parameter(description = "Imagem do produto")
            @RequestPart(value = "imagem", required = false) MultipartFile imagemProduto,
            @Parameter(description = "Informações de autenticação do usuário", hidden = true)
            Authentication authentication
            ) {
        
        if (!authenticationUtils.isAdminOrEmployee(authentication)) {
            throw new org.springframework.security.access.AccessDeniedException(
                "Acesso negado. Apenas administradores e funcionários podem criar produtos."
            );
        }

          Produto produtoCriado = criarProdutoCase.execute(
                  new CriarProdutoInput(dto.getModelo(), dto.getMarca(), dto.getUrlFrabricante(), dto.getDescricao(), dto.getQtdEstoque(), dto.getCategoriaId()),
                  imagemProduto
          );
        
        return ProdutoEntityMapper.toProdutoGenericoRespostaDTO(ProdutoEntityMapper.toProdutoEntity(produtoCriado));
      }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Atualizar um produto",
            description = "Atualiza os dados de um produto existente a partir do seu ID. " +
                         "Acesso restrito apenas para administradores e funcionários."
    )
    public ProdutoRespostaDTO atualizarProduto(
            @Parameter(description = "ID do produto a ser atualizado", required = true)
            @PathVariable @Positive Integer id,
            @Valid @RequestBody ProdutoAtualizacaoDTO dto,
            @Parameter(description = "Informações de autenticação do usuário", hidden = true)
            Authentication authentication) {
        
        if (!authenticationUtils.isAdminOrEmployee(authentication)) {
            throw new org.springframework.security.access.AccessDeniedException(
                "Acesso negado. Apenas administradores e funcionários podem editar produtos."
            );
        }

        Produto produtoAtualizado = atualizarProduto.execute(id, new AtualizarProdutoInput(
                dto.getModelo(),
                dto.getMarca(),
                dto.getUrlFrabricante(),
                dto.getDescricao(),
                dto.getQtdEstoque(),
                dto.getCategoriaId(),
                dto.isAtivo()
        ));

        return ProdutoEntityMapper.toProdutoGenericoRespostaDTO(produtoAtualizado);
    }

    @PutMapping(value = "/{id}/imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Atualizar imagem do produto",
            description = "Atualiza a imagem de um produto existente. Se o produto já possui uma imagem, ela será substituída pela nova. " +
                         "Acesso restrito apenas para administradores e funcionários."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    encoding = @Encoding(name = "imagem", contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
            )
    )
    @Transactional
    public void atualizarImagemProduto(
            @Parameter(description = "ID do produto cuja imagem será atualizada", required = true)
            @PathVariable @Positive Integer id,
            @Parameter(description = "Nova imagem do produto", required = true)
            @RequestPart("imagem") MultipartFile novaImagem,
            @Parameter(description = "Informações de autenticação do usuário", hidden = true)
            Authentication authentication) {
        
        if (!authenticationUtils.isAdminOrEmployee(authentication)) {
            throw new org.springframework.security.access.AccessDeniedException(
                "Acesso negado. Apenas administradores e funcionários podem atualizar imagens de produtos."
            );
        }
        
         atualizarImagemProdutoCase.execute(novaImagem, id);
    }

    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Remover um produto",
            description = "Remove um produto do sistema a partir do seu ID. " +
                         "Acesso restrito apenas para administradores e funcionários."
    )
    public void removerProduto(
            @Parameter(description = "ID do produto a ser removido", required = true)
            @PathVariable @Positive Integer id,
            @Parameter(description = "Informações de autenticação do usuário", hidden = true)
            Authentication authentication) {
        
        if (!authenticationUtils.isAdminOrEmployee(authentication)) {
            throw new org.springframework.security.access.AccessDeniedException(
                "Acesso negado. Apenas administradores e funcionários podem remover produtos."
            );
        }
        
        excluirProduto.execute(id);
    }
}
