package com.infinitysolutions.applicationservice.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenExpiradoException extends ApplicationServiceException {
    public TokenExpiradoException(String message) {
        super("token_expirado", message);
    }

    public static TokenExpiradoException tokenExpirado() {
        return new TokenExpiradoException("O tempo de acesso expirou, por favor refaça o login.");
    }
}
