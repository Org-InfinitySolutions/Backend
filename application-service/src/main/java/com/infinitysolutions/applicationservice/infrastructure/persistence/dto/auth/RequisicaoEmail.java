package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth;

import com.infinitysolutions.applicationservice.infrastructure.utils.validation.EmailValido;
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
