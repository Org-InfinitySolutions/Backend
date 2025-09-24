package com.infinitysolutions.applicationservice.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class AuthServiceCommunicationException extends ApplicationServiceException {

    public AuthServiceCommunicationException(String message) {
        super("auth_service_communication_error", message);
    }

    public AuthServiceCommunicationException(String message, Throwable cause) {
        super("auth_service_communication_error", message, cause);
    }

    public static AuthServiceCommunicationException falhaAoEnviarCredenciais() {
        return new AuthServiceCommunicationException(
                "Não foi possível completar o cadastro devido a um problema de comunicação interna. Por favor, tente novamente em alguns instantes.");
    }

    public static AuthServiceCommunicationException timeout() {
        return new AuthServiceCommunicationException(
                "O serviço está temporariamente sobrecarregado. Por favor, tente novamente em alguns instantes.");
    }

    public static AuthServiceCommunicationException servicoIndisponivel() {
        return new AuthServiceCommunicationException(
                "O serviço de autenticação está temporariamente indisponível. Sua solicitação não pôde ser concluída neste momento.");
    }
}
