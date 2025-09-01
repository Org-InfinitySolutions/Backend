package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth;

import com.infinitysolutions.applicationservice.infra.validation.EmailValido;
import com.infinitysolutions.applicationservice.infra.validation.SenhaValida;
import io.swagger.v3.oas.annotations.media.Schema;

public record RequisicaoLogin(
        @EmailValido
        @Schema(
                description = "Email do usuário para autenticação",
                example = "usuario@exemplo.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String email,
        @SenhaValida(
                message = "A senha deve conter no mínimo 8 caracteres, incluindo letras e números e ao menos uma letra maiúscula.",
                requireUppercase = true
        )
        @Schema(
                description = "Senha do usuário",
                example = "Senha123",
                type = "string",
                format = "password",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String senha
) {
}
