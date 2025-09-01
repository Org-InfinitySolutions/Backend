package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth;

public record RespostaLogin(
        String token,
        Integer tempoExpiracao
) {
}
