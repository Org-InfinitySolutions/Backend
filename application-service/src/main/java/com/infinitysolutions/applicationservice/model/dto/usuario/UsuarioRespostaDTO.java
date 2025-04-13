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

    protected UsuarioRespostaDTO(
            UUID id, 
            String nome, 
            String telefone,
            String tipo,
            EnderecoDTO endereco) {
        this.id = id;
        this.nome = nome;
        this.telefoneCelular = telefone;
        this.endereco = endereco;
        this.tipo = tipo;
    }
}