package com.infinitysolutions.authservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.infinitysolutions.authservice.infra.validation.EmailValido;
import com.infinitysolutions.authservice.infra.validation.SenhaValida;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


import java.util.UUID;

public record RequisicaoCadastro(
        @EmailValido
        @Schema(
                description = "Email para login do usuário",
                example = "usuario@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String email,
        @SenhaValida(
                message = "A senha deve conter no mínimo 8 caracteres, incluindo letras e números e ao menos uma letra maiúscula.",
                requireUppercase = true
        )
        @Schema(
                description = "Senha do usuário. Deve conter no mínimo 8 caracteres, incluindo letras, números e pelo menos uma letra maiúscula",
                example = "Senha123",
                type = "string",
                format = "password",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String senha,
        @NotNull(message = "O ID do usuário é obrigatório")
        @Schema(
                description = "ID único do usuário no sistema",
                example = "123e4567-e89b-12d3-a456-426614174000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @JsonProperty("id_usuario")
        UUID idUsuario
) {
}
