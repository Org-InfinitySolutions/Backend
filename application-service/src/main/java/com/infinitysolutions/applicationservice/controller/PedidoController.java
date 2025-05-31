package com.infinitysolutions.applicationservice.controller;

import com.infinitysolutions.applicationservice.infra.utils.AuthenticationUtils;
import com.infinitysolutions.applicationservice.model.dto.pedido.*;
import com.infinitysolutions.applicationservice.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    private final PedidoService service;
    private final AuthenticationUtils authUtil;

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
        return service.cadastrar(dto, documentoAuxiliar, UUID.fromString(auth.getName()));
    }


    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
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
            @Valid @RequestBody PedidoAtualizacaoSituacaoDTO dto
            ) {
        return service.atualizarSituacao(id, dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Listar pedidos do sistema",
        description = "Retorna uma lista com todos os pedidos registrados. " +
                     "As informações dos pedidos variam de acordo com o perfil do usuário " +
                     "(usuário comum ou administrador).")

    public List<PedidoRespostaDTO> listar(Authentication auth) {
        return authUtil.isAdmin(auth) ? service.listarAdmin() : service.listar(UUID.fromString(auth.getName()));
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
        return authUtil.isAdmin(auth) ? service.buscarPorIdAdmin(id) : service.buscarPorId(id, UUID.fromString(auth.getName()));
    }
}
