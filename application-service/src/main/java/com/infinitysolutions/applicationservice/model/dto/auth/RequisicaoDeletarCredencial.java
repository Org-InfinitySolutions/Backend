package com.infinitysolutions.applicationservice.model.dto.auth;

import com.infinitysolutions.applicationservice.infra.validation.SenhaValida;
import io.swagger.v3.oas.annotations.media.Schema;

public record RequisicaoDeletarCredencial(
        @Schema(
                description = "Senha do usuário",
                example = "Senha123",
                type = "string",
                format = "password",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @SenhaValida(
                message = "A senha deve conter no mínimo 8 caracteres, incluindo letras e números e ao menos uma letra maiúscula.",
                requireUppercase = true
        )
        String senha
) {
}
