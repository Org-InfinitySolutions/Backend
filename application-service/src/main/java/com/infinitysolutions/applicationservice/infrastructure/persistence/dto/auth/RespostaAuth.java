package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record RespostaAuth(
        @Schema(
                description = "Token JWT de acesso",
                example = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @JsonProperty("access_token")
        String acessToken,
        @Schema(
                description = "Tipo do token",
                example = "Bearer",
                defaultValue = "Bearer",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @JsonProperty("token_type")
        String tokenType,
        @Schema(
                description = "Tempo de validade do token em segundos",
                example = "3600",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @JsonProperty("expires_in")
        Integer expiresIn
) {
}
