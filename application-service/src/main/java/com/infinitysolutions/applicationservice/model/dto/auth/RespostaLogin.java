package com.infinitysolutions.applicationservice.model.dto.auth;

public record RespostaLogin(
        String token,
        Integer tempoExpiracao
) {
}
