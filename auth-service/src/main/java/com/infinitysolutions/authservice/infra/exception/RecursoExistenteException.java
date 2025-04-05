package com.infinitysolutions.authservice.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.CONFLICT)
public class RecursoExistenteException extends AuthServiceException {

    public RecursoExistenteException(String mensagem) {
        super("recurso_existente", mensagem);
    }

    public static RecursoExistenteException emailJaEmUso(String email) {
        return new RecursoExistenteException("Email já está em uso: " + email);
    }

    public static RecursoExistenteException credencialJaExiste(UUID idUsuario) {
        return new RecursoExistenteException("Credencial já existe para o usuário com ID: " + idUsuario);
    }
}