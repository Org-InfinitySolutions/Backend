package com.infinitysolutions.applicationservice.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.CONFLICT)
public class RecursoExistenteException extends ApplicationServiceException {
    public RecursoExistenteException(String message) {
        super("recurso_existente", message);
    }

    public static RecursoExistenteException cpfJaEmUso(String cpf) {
        return new RecursoExistenteException("CPF já está em uso: " + cpf);
    }
    
    public static RecursoExistenteException rgJaEmUso(String rg) {
        return new RecursoExistenteException("RG já está em uso: " + rg);
    }

    public static RecursoExistenteException cnpjJaEmUso(String cnpj) {
        return new RecursoExistenteException("CNPJ já está em uso: " + cnpj);
    }
    
    public static RecursoExistenteException emailJaEmUso(String email) {
        return new RecursoExistenteException("Email já está em uso: " + email);
    }
    
    public static RecursoExistenteException credencialJaExiste(UUID idUsuario) {
        return new RecursoExistenteException("Credencial já existe para o usuário com ID: " + idUsuario);
    }
}
