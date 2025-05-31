package com.infinitysolutions.applicationservice.model.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.infinitysolutions.applicationservice.model.dto.endereco.EnderecoResumidoDTO;
import com.infinitysolutions.applicationservice.model.dto.pessoa.fisica.PessoaFisicaRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.pessoa.juridica.PessoaJuridicaRespostaCadastroDTO;
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
        @JsonSubTypes.Type(value = PessoaFisicaRespostaCadastroDTO.class, name = "PF"),
        @JsonSubTypes.Type(value = PessoaJuridicaRespostaCadastroDTO.class, name = "PJ")
})
@Schema(description = "Informações de resposta após criação de usuário")
@Data
@NoArgsConstructor
public abstract class UsuarioRespostaCadastroDTO {
    private UUID id;

    private String nome;

    @JsonProperty("telefone_celular")
    private String telefone;

    private EnderecoResumidoDTO endereco;

    protected UsuarioRespostaCadastroDTO(UUID id, String nome, String telefone, EnderecoResumidoDTO enderecoResumidoDTO) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = enderecoResumidoDTO;
    }
}