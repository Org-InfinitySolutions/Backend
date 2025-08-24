package com.infinitysolutions.applicationservice.model.dto.auth;

import com.infinitysolutions.applicationservice.infra.validation.EmailValido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados necessários para alteração de email do usuário")
public record RequisicaoAlterarEmail(
        @NotBlank(message = "A senha é obrigatória para confirmar a alteração")
        @Schema(
                description = "Senha atual do usuário para validação da alteração",
                example = "MinhaSenh@Atual123",
                type = "string",
                format = "password",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String senha,
        
        @NotBlank(message = "O novo email é obrigatório")
        @EmailValido
        @Schema(
                description = "Novo endereço de email que substituirá o atual",
                example = "novoemail@exemplo.com",
                type = "string",
                format = "email",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String novoEmail
) {
}
