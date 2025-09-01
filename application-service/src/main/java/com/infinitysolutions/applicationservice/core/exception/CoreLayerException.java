package com.infinitysolutions.applicationservice.core.exception;

public class CoreLayerException extends RuntimeException {
    private final String codigo;

    protected CoreLayerException(String codigo, String message) {
        super(message);
        this.codigo = codigo;
    }

    protected CoreLayerException(String codigo, String message, Throwable cause) {
        super(message, cause);
        this.codigo = codigo;
    }
}
