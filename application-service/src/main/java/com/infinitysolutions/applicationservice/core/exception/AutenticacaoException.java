package com.infinitysolutions.applicationservice.core.exception;

import com.infinitysolutions.applicationservice.infrastructure.exception.ApplicationServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AutenticacaoException extends ApplicationServiceException {
    
    public AutenticacaoException(String mensagem) {
        super("credenciais_invalidas", mensagem);
    }
    
    public static AutenticacaoException credenciaisInvalidas() {
        return new AutenticacaoException("Credenciais incorretas");
    }
    
    public static AutenticacaoException tokenInvalido() {
        return new AutenticacaoException("Token inválido ou expirado");
    }

    public static AutenticacaoException acessoNegado() {
        return new AutenticacaoException("Acesso não autorizado ao recurso");
    }
}