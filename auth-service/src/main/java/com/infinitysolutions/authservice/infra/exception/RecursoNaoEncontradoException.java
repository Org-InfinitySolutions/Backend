package com.infinitysolutions.authservice.infra.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoNaoEncontradoException extends AuthServiceException {

    public RecursoNaoEncontradoException(String mensagem) {
        super("recurso_nao_encontrado", mensagem);
    }

    public static RecursoNaoEncontradoException credencialNaoEncontrada(UUID idUsuario) {
        return new RecursoNaoEncontradoException("Credencial não encontrada para usuário com ID: " + idUsuario);
    }

    public static RecursoNaoEncontradoException credencialNaoEncontrada(String email) {
        return new RecursoNaoEncontradoException("Credencial não encontrada para usuário com email: " + email);
    }

}
