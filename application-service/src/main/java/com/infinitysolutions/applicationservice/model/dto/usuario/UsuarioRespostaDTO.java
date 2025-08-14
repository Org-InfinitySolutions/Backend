package com.infinitysolutions.applicationservice.model.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.infinitysolutions.applicationservice.model.dto.endereco.EnderecoDTO;
import com.infinitysolutions.applicationservice.model.dto.pessoa.fisica.PessoaFisicaDTO;
import com.infinitysolutions.applicationservice.model.dto.pessoa.juridica.PessoaJuridicaDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PessoaFisicaDTO.class, name = "PF"),
        @JsonSubTypes.Type(value = PessoaJuridicaDTO.class, name = "PJ")
})
@Schema(description = "Informações de resposta do usuário completo")
@Data
@NoArgsConstructor
public abstract class UsuarioRespostaDTO {
    @Schema(
            description = "Identificador único do usuário",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
    )
    private UUID id;

    @Schema(
            description = "Nome do usuário ou razão social",
            example = "João da Silva Santos"
    )
    private String nome;
    
    @Schema(
            description = "Email do usuário",
            example = "usuario@example.com"
    )
    private String email;
    
    @Schema(
            description = "Telefone celular de contato com DDD",
            example = "(11) 98765-4321"
    )
    @JsonProperty("telefone_celular")
    private String telefoneCelular;
    
    @Schema(description = "Endereço do usuário")
    private EnderecoDTO endereco;

    @Schema(
            description = "Tipo de usuário (PF para Pessoa Física ou PJ para Pessoa Jurídica)",
            example = "PF ou PJ",
            allowableValues = {"PF", "PJ"}
    )
    private String tipo;

    @Schema(
            description = "Data de criação do usuário",
            example = "2023-10-01T12:00:00"
    )
    @JsonProperty("data_criacao")
    private LocalDateTime dataCriacao;

    @Schema(
            description = "Data da última atualização do usuário",
            example = "2023-10-01T12:00:00"
    )
    @JsonProperty("data_atualizacao")
    private LocalDateTime dataAtualizacao;

    private List<DocumentoUsuarioDTO> documentos;

    protected UsuarioRespostaDTO(
            UUID id, 
            String nome, 
            String email,
            String telefone,
            String tipo,
            EnderecoDTO endereco,
            LocalDateTime dataCriacao,
            LocalDateTime dataAtualizacao,
            List<DocumentoUsuarioDTO> documentos
            ) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefoneCelular = telefone;
        this.endereco = endereco;
        this.tipo = tipo;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.documentos = documentos;
    }

    public record DocumentoUsuarioDTO(
            String originalFilename,
            String downloadUrl,
            String mimeType,
            String tipoAnexo
    ){

    }
}