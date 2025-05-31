package com.infinitysolutions.applicationservice.controller;


import com.infinitysolutions.applicationservice.model.dto.produto.ProdutoCriacaoDTO;
import com.infinitysolutions.applicationservice.model.dto.produto.ProdutoRespostaDTO;
import com.infinitysolutions.applicationservice.model.enums.TipoAnexo;
import com.infinitysolutions.applicationservice.service.DocumentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios/documentos")
@AllArgsConstructor
@Validated
@Tag(name = "Documentos", description = "Endpoints para gerenciamento dos documentos dos usuários")
public class DocumentosController {

    private final DocumentoService service;

    @PostMapping(path = "/{idUsuario}" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Adicionar um novo documento a um usuário.",
            description = "Cria um novo documento no sistema a e o vincula a um usuário"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    encoding = {
                            @Encoding(name = "dados", contentType = MediaType.APPLICATION_JSON_VALUE),
                            @Encoding(name = "documento", contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    }
            )
    )
    public void adicionarDocumentoUsuario(
            @Parameter(description = "Dados do documento em JSON", required = true)
            @RequestPart("dados") @Valid DadosDocumentoCriacaoDTO dto,
            @Parameter(description = "Documento do usuário")
            @RequestPart(value = "documento", required = true) MultipartFile documento,
            @PathVariable UUID idUsuario
            ) {
        service.adicionarDocumentoUsuario(documento, dto, idUsuario);
    }


    @Schema(
            name = "DadosDocumentoCriacao",
            description = "Dados necessários para criação de um novo documento no sistema"
    )
    public record DadosDocumentoCriacaoDTO(
            @Schema(
                    description = "Tipo do anexo do documento (COPIA_RG, COPIA_CNPJ, ETC)",
                    example = "COPIA_RG",
                    required = true
            )
            @NotNull(message = "Tipo do pedido é obrigatório")
            TipoAnexo tipoAnexo
    ){

    }

}
