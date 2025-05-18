package com.infinitysolutions.authservice.model.dto;

import com.infinitysolutions.authservice.infra.validation.EmailValido;
import io.swagger.v3.oas.annotations.media.Schema;

public record RequisicaoEmail(
        @EmailValido
        @Schema(
                description = "Email para validar se existe",
                example = "usuario@exemplo.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String email
) {
}
