package com.infinitysolutions.applicationservice.infrastructure.controller;

import com.infinitysolutions.applicationservice.core.domain.pedido.Pedido;
import com.infinitysolutions.applicationservice.core.domain.usuario.Credencial;
import com.infinitysolutions.applicationservice.core.usecases.credencial.BuscarCredencialPorId;
import com.infinitysolutions.applicationservice.core.usecases.endereco.EnderecoInput;
import com.infinitysolutions.applicationservice.core.usecases.pedido.*;
import com.infinitysolutions.applicationservice.core.valueobject.PageResult;
import com.infinitysolutions.applicationservice.infrastructure.mapper.PedidoMapper;
import com.infinitysolutions.applicationservice.infrastructure.mapper.UsuarioEntityMapper;
import com.infinitysolutions.applicationservice.infrastructure.service.S3FileUploadService;
import com.infinitysolutions.applicationservice.infrastructure.utils.AuthenticationUtils;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pedido.*;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Validated
@Tag(
    name = "Pedidos", 
    description = "API para gerenciamento de pedidos de produtos. " +
                 "Permite criar novos pedidos, listar pedidos por usuário e gerenciar o ciclo de vida dos pedidos."
)
public class PedidoController {

    private final UsuarioEntityMapper usuarioEntityMapper;
    private final AuthenticationUtils authUtil;
    private final CadastrarPedido cadastrarPedido;
    private final AtualizarSituacaoPedido atualizarSituacao;
    private final ListarTodosPedidos listarTodosPedidos;
    private final BuscarPedidoPorId buscarPedidoPorId;
    private final BuscarCredencialPorId buscarCredencialPorId;
    private final S3FileUploadService fileUploadService;

    @Transactional
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Criar um novo pedido",
        description = "Cria um novo pedido no sistema com os produtos especificados."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(
            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
            encoding = {
                @Encoding(name = "pedido", contentType = MediaType.APPLICATION_JSON_VALUE),
                @Encoding(name = "documento_auxiliar", contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
            }
        )
    )
    public PedidoRespostaCadastroDTO cadastrar(
        @Parameter(description = "Dados do pedido em JSON", required = true)
        @RequestPart("pedido") @Valid PedidoCadastroDTO dto,
        @Parameter(description = "Documento auxiliar (PDF ou imagem)", required = false)
        @RequestPart(value = "documento_auxiliar", required = false) MultipartFile documentoAuxiliar,
        Authentication auth
    ) {
        List<CadastrarPedidoInput.ProdutoPedidoInput> produtoPedidoInputList = dto.produtos().stream().
                map(produtoPedidoDTO -> new CadastrarPedidoInput.ProdutoPedidoInput(produtoPedidoDTO.produtoId(), produtoPedidoDTO.quantidade()))
                .toList();
        EnderecoInput enderecoInput = new EnderecoInput(dto.endereco().getCep(), dto.endereco().getLogradouro(), dto.endereco().getBairro(), dto.endereco().getCidade(), dto.endereco().getEstado(), dto.endereco().getNumero(), dto.endereco().getComplemento());
        CadastrarPedidoInput cadastrarPedidoInput = new CadastrarPedidoInput(produtoPedidoInputList, enderecoInput, dto.tipo(), dto.dataEntrega(), dto.dataRetirada(), dto.descricao());
        Pedido pedidoCadastrado = cadastrarPedido.execute(cadastrarPedidoInput, documentoAuxiliar, UUID.fromString(auth.getName()));
        return PedidoMapper.toPedidoRespostaCadastroDTO(pedidoCadastrado);
    }


    @Transactional
    @PutMapping("/{id}/situacao")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Atualizar situação do pedido",
            description = "Atualiza a situação de um pedido específico. " +
                    "Restrição: pedidos só podem ser cancelados quando estão em análise."
    )
    public PedidoRespostaDTO atualizarSituacao(
            @Parameter(description = "ID do pedido a ser atualizado", required = true)
            @PathVariable @Positive Integer id,
            @Parameter(description = "Nova situação do pedido", required = true)
            @Valid @RequestBody PedidoAtualizacaoSituacaoDTO dto,
            Authentication auth
            ) {
        Pedido pedido = atualizarSituacao.execute(id, UUID.fromString(auth.getName()), dto.situacao(), authUtil.isCustomer(auth));
        return PedidoMapper.toPedidoRespostaDTO(pedido);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Listar pedidos do sistema",
            description = "Retorna uma lista com todos os pedidos registrados com suporte a paginação. " +
                    "As informações variam conforme o perfil do usuário (comum ou administrador)."
    )
    public PageResult<PedidoRespostaDTO> listar(
            Authentication auth,
            @Parameter(description = "Número da página (começa em 0)", example = "0")
            @RequestParam(defaultValue = "0") int offset,
            @Parameter(description = "Quantidade de itens por página", example = "10")
            @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Ordenação (campo,direção)", example = "id,desc")
            @RequestParam(defaultValue = "id,desc") String sort
    ) {
        boolean isAdmin = authUtil.isAdminOrEmployee(auth);
        UUID usuarioId = UUID.fromString(auth.getName());

        PageResult<Pedido> pagePedidos = listarTodosPedidos.execute(isAdmin, usuarioId, offset, limit, sort);

        List<PedidoRespostaDTO> pedidosDTO = isAdmin
                ? PedidoMapper.toPedidoRespostaAdminDTOList(pagePedidos.getContent())
                : PedidoMapper.toPedidoRespostaDTOList(pagePedidos.getContent());

        return new PageResult<>(
                pedidosDTO,
                pagePedidos.getTotalElements(),
                pagePedidos.getOffset(),
                pagePedidos.getLimit()
        );
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Buscar um pedido por id",
            description = "Faz uma busca no sistema por um pedido com base em um id, as informações vão variar de acordo com o perfil do usuário."
    )
    public PedidoRespostaDetalhadoDTO buscarPorId(
                                         Authentication auth,
                                         @Parameter(description = "ID do pedido", required = true)
                                         @PathVariable @Positive Integer id
                                         ) {
        Pedido pedidoEncontrado = buscarPedidoPorId.execute(id, UUID.fromString(auth.getName()), authUtil.isAdmin(auth));
        Credencial credencialEncontrada = buscarCredencialPorId.execute(pedidoEncontrado.getUsuario().getId());
        List<PedidoRespostaDetalhadoDTO.DocumentoPedidoDTO> documentos = pedidoEncontrado.getDocumentos().stream().map(documento -> new PedidoRespostaDetalhadoDTO.DocumentoPedidoDTO(
                documento.getOriginalFilename(),
                fileUploadService.generatePrivateFilePresignedUrl(documento.getBlobName(), 60),
                documento.getMimeType()
        )).toList();

        return PedidoMapper.toPedidoRespostaDetalhadoAdminDTO(pedidoEncontrado, usuarioEntityMapper.toUsuarioRespostaDTO(pedidoEncontrado.getUsuario(), credencialEncontrada), documentos);
    }
}
