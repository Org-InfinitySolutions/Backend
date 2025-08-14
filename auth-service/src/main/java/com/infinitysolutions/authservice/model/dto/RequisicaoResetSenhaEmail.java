package com.infinitysolutions.authservice.model.dto;

import com.infinitysolutions.authservice.infra.validation.EmailValido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Requisição para envio de código de reset de senha por email")
public record RequisicaoResetSenhaEmail(
        @EmailValido
        @NotBlank(message = "Email é obrigatório")
        @Schema(
                description = "Email do usuário que esqueceu a senha",
                example = "usuario@exemplo.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String email
) {
}
