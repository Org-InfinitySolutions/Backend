package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.infinitysolutions.applicationservice.infra.validation.TelefoneValido;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.endereco.EnderecoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.fisica.PessoaFisicaAtualizacaoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.juridica.PJAtualizacaoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PessoaFisicaAtualizacaoDTO.class, name = "PF"),
        @JsonSubTypes.Type(value = PJAtualizacaoDTO.class, name = "PJ")
})
@Schema(description = "Informações base para a atualizacao de usuários")
public abstract class UsuarioAtualizacaoDTO {

    @Size(min = 3, max = 255, message = "O nome deve ter entre {min} e {max} caracteres")
    @Schema(
            description = "Nome completo do usuário",
            example = "João da Silva Santos",
            minLength = 3,
            maxLength = 255,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O telefone celular é obrigatório")
    @TelefoneValido(fixo = false, message = "Telefone celular inválido. Formato esperado: (99) 99999-9999")
    @Schema(
            description = "Telefone celular de contato com DDD",
            example = "(11) 99999-9999",
            pattern = "(99) 99999-9999 ou 9999999999",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("telefone_celular")
    private String telefoneCelular;

    @Schema(
            description = "Tipo de usuário (PF para Pessoa Física ou PJ para Pessoa Jurídica)",
            example = "PF",
            allowableValues = {"PF", "PJ"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String tipo;


    @Schema(
            description = "Dados do endereço do usuário",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Valid
    private EnderecoDTO endereco;
}
