package com.infinitysolutions.applicationservice.infra.exception;

public abstract class ApplicationServiceException extends RuntimeException {
    private final String codigo;

    protected ApplicationServiceException(String codigo, String message) {
        super(message);
        this.codigo = codigo;
    }

    protected ApplicationServiceException(String codigo, String message, Throwable cause) {
        super(message, cause);
        this.codigo = codigo;
    }
}
