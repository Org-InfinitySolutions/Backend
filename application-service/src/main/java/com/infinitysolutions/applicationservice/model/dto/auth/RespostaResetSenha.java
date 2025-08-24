package com.infinitysolutions.applicationservice.model.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta para operações de reset de senha")
public record RespostaResetSenha(
        @Schema(
                description = "Indica se a operação foi executada com sucesso",
                example = "true"
        )
        boolean sucesso,
        
        @Schema(
                description = "Mensagem informativa sobre o resultado da operação",
                example = "Código de reset de senha enviado com sucesso para o email fornecido"
        )
        String mensagem
) {
}