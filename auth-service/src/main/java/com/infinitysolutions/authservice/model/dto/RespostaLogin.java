package com.infinitysolutions.authservice.model.dto;

public record RespostaLogin(
        String token,
        Integer tempoExpiracao
) {
}
