package com.infinitysolutions.authservice.infra.exception;

public class ErroInesperadoException extends AuthServiceException {
    public ErroInesperadoException(String message) {
        super("recurso_existente", message);
    }

    public static ErroInesperadoException erroInesperado(String mensagem, String erro) {
        return new ErroInesperadoException(mensagem + " erro: " + erro);
    }
}
