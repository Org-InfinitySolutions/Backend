package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth;

import com.infinitysolutions.applicationservice.infra.validation.EmailValido;
import com.infinitysolutions.applicationservice.infra.validation.SenhaValida;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Requisição para validar código de reset e definir nova senha")
public record RequisicaoResetSenhaCodigo(
        @EmailValido
        @NotBlank(message = "Email é obrigatório")
        @Schema(
                description = "Email do usuário que solicitou o reset",
                example = "usuario@exemplo.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String email,
        
        @NotBlank(message = "Código de verificação é obrigatório")
        @Schema(
                description = "Código de verificação recebido por email",
                example = "ABC123DEF456",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String codigo,
        
        @SenhaValida(
                message = "A senha deve conter no mínimo 8 caracteres, incluindo letras e números e ao menos uma letra maiúscula.",
                requireUppercase = true
        )
        @Schema(
                description = "Nova senha do usuário",
                example = "NovaSenha123",
                type = "string",
                format = "password",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String novaSenha
) {
}