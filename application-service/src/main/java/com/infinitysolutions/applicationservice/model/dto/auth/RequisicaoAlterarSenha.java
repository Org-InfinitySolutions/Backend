package com.infinitysolutions.applicationservice.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados necessários para alteração de senha do usuário")
public record RequisicaoAlterarSenha(
        @NotBlank(message = "A senha atual é obrigatória")
        @Schema(
                description = "Senha atual do usuário para validação",
                example = "MinhaSenh@Atual123",
                type = "string",
                format = "password",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String senhaAtual,
        
        @NotBlank(message = "A nova senha é obrigatória")
        @Size(min = 8, message = "A nova senha deve ter pelo menos 8 caracteres")
        @Schema(
                description = "Nova senha que substituirá a atual (mínimo 8 caracteres)",
                example = "MinhaNov@Senha456",
                type = "string",
                format = "password",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minLength = 8
        )
        String novaSenha
) {
}
