package com.infinitysolutions.applicationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PessoaFisicaDTO.class, name = "PF"),
        @JsonSubTypes.Type(value = PessoaJuridicaDTO.class, name = "PJ")
})
@Schema(description = "Informações de resposta do usuário completo")
@Data
@NoArgsConstructor
public abstract class UsuarioRespostaDTO {
    @Schema(description = "Identificador único do usuário")
    private UUID id;

    @Schema(description = "Nome do usuário ou razão social")
    private String nome;
    
    @Schema(description = "Telefone celular de contato com DDD", example = "(11) 98765-4321")
    @JsonProperty("telefone_celular")
    private String telefoneCelular;
    
    @Schema(description = "Endereço do usuário")
    private EnderecoDTO endereco;

    protected UsuarioRespostaDTO(
            UUID id, 
            String nome, 
            String telefone,
            EnderecoDTO endereco) {
        this.id = id;
        this.nome = nome;
        this.telefoneCelular = telefone;
        this.endereco = endereco;
    }
}