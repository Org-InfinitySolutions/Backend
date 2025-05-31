package com.infinitysolutions.applicationservice.controller.produto;

import com.infinitysolutions.applicationservice.model.dto.produto.ProdutoCriacaoDTO;
import com.infinitysolutions.applicationservice.model.dto.produto.ProdutoRespostaDTO;
import com.infinitysolutions.applicationservice.service.produto.ProdutoService;
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
    private final ProdutoService produtoService;
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Listar todos os produtos",
            description = "Retorna uma lista com todos os produtos cadastrados no sistema."
    )
    public List<ProdutoRespostaDTO> listarTodosProdutos() {
        return produtoService.listarTodosProdutos();
    }
    
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Buscar produto por ID",
            description = "Retorna os detalhes completos de um produto específico a partir do seu ID"
    )
    public ProdutoRespostaDTO buscarProdutoPorId(
            @Parameter(description = "ID do produto a ser buscado", required = true)
            @PathVariable @Positive Integer id) {
        return produtoService.buscarPorId(id);
    }
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Criar um novo produto",
            description = "Cria um novo produto no sistema a partir dos dados fornecidos"
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
            @RequestPart(value = "imagem", required = false) MultipartFile imagemProduto
            ) {
        return produtoService.criar(dto, imagemProduto);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Atualizar um produto",
            description = "Atualiza os dados de um produto existente a partir do seu ID"
    )

    public ProdutoRespostaDTO atualizarProduto(
            @Parameter(description = "ID do produto a ser atualizado", required = true)
            @PathVariable @Positive Integer id,
            @Valid @RequestBody ProdutoCriacaoDTO dto) {
        return produtoService.atualizar(id, dto);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Remover um produto",
            description = "Remove um produto do sistema a partir do seu ID"
    )
    public void removerProduto(
            @Parameter(description = "ID do produto a ser removido", required = true)
            @PathVariable @Positive Integer id) {
        produtoService.remover(id);
    }
}
