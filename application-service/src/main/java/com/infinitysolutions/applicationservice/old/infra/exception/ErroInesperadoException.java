package com.infinitysolutions.applicationservice.old.infra.exception;

public class ErroInesperadoException extends ApplicationServiceException {
    public ErroInesperadoException(String message) {
        super("recurso_existente", message);
    }

    public static ErroInesperadoException erroInesperado(String mensagem, String erro) {
        return new ErroInesperadoException(mensagem + " erro: " + erro);
    }
}
