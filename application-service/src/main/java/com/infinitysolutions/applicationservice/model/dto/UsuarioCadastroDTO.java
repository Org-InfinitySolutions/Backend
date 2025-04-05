package com.infinitysolutions.applicationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.infinitysolutions.applicationservice.infra.validation.EmailValido;
import com.infinitysolutions.applicationservice.infra.validation.SenhaValida;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
        @JsonSubTypes.Type(value = PessoaFisicaCadastroDTO.class, name = "PF"),
        @JsonSubTypes.Type(value = PessoaJuridicaCadastroDTO.class, name = "PJ")
})
@Schema(description = "Informações base para cadastro de usuários")
public abstract class UsuarioCadastroDTO {
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 255, message = "O nome deve ter entre {min} e {max} caracteres")
        @Schema(
                description = "Nome completo do usuário",
                example = "João da Silva Santos",
                minLength = 3,
                maxLength = 255,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        private String nome;

        @NotBlank(message = "O telefone é obrigatório")
        @Pattern(
                regexp = "^\\(?[1-9]{2}\\)? ?(?:[2-8]|9[1-9])[0-9]{3}\\-?[0-9]{4}$",
                message = "Telefone inválido. Formatos aceitos: (99) 99999-9999 ou 99999999999"
        )
        @Schema(
                description = "Telefone de contato com DDD",
                example = "(11) 98765-4321",
                pattern = "(99) 99999-9999 ou 99999999999",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        private String telefone;

        @Schema(
                description = "Tipo de usuário (PF para Pessoa Física ou PJ para Pessoa Jurídica)",
                example = "PF",
                allowableValues = {"PF", "PJ"},
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        private String tipo;

        @EmailValido
        @Schema(description = "Email do usuário", example = "usuario@exemplo.com", requiredMode = Schema.RequiredMode.REQUIRED)
        private String email;

        @SenhaValida(
                message = "A senha deve conter no mínimo 8 caracteres, incluindo letras e números e ao menos uma letra maiúscula.",
                requireUppercase = true
        )
        @Schema(description = "Senha do usuário",
                example = "Senha123",
                type = "string", 
                format = "password",
                requiredMode = Schema.RequiredMode.REQUIRED)
        private String senha;

        @Valid
        @Schema(
                description = "Dados do endereço do usuário",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        private EnderecoDTO endereco;
}
