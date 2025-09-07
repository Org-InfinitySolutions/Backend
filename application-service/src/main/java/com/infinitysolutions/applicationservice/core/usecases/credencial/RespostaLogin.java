package com.infinitysolutions.applicationservice.core.usecases.credencial;

public record RespostaLogin(
        String token,
        Integer tempoExpiracao
) {
}
