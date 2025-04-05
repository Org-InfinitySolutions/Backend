package com.infinitysolutions.authservice.infra.exception;

import lombok.Getter;

@Getter
public abstract class AuthServiceException extends RuntimeException {
    private final String codigo;

    public AuthServiceException(String codigo, String mensagem) {
        super(mensagem);
        this.codigo = codigo;
    }

    public AuthServiceException(String codigo, String mensagem, Throwable cause) {
        super(mensagem, cause);
        this.codigo = codigo;
    }
}
