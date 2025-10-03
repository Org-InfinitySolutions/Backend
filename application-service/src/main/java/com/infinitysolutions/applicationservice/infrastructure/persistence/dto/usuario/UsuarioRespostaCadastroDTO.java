package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.endereco.EnderecoResumidoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.fisica.PFRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.juridica.PJRespostaCadastroDTO;
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
        @JsonSubTypes.Type(value = PFRespostaCadastroDTO.class, name = "PF"),
        @JsonSubTypes.Type(value = PJRespostaCadastroDTO.class, name = "PJ")
})
@Schema(description = "Informações de resposta após criação de usuário")
@Data
@NoArgsConstructor
public abstract class UsuarioRespostaCadastroDTO {
    private UUID id;

    private String nome;

    @JsonProperty("telefone_celular")
    private String telefone;

    private String email;

    private EnderecoResumidoDTO endereco;

    protected UsuarioRespostaCadastroDTO(UUID id, String nome, String telefone, String email, EnderecoResumidoDTO enderecoResumidoDTO) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.endereco = enderecoResumidoDTO;
    }
}