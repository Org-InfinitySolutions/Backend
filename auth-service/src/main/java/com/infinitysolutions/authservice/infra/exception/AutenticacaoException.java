package com.infinitysolutions.authservice.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AutenticacaoException extends AuthServiceException {
    
    public AutenticacaoException(String mensagem) {
        super("credenciais_invalidas", mensagem);
    }
    
    public static AutenticacaoException credenciaisInvalidas() {
        return new AutenticacaoException("Email ou senha incorretos");
    }
    
    public static AutenticacaoException tokenInvalido() {
        return new AutenticacaoException("Token inválido ou expirado");
    }
}