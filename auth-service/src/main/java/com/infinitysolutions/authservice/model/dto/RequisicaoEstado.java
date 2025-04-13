package com.infinitysolutions.authservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record RequisicaoEstado(
        @Schema(description = "ID do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull
        boolean ativo
) {
}
