package com.infinitysolutions.applicationservice.old.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class RecursoIncompativelException extends ApplicationServiceException {
    public RecursoIncompativelException(String message) {
        super("recurso_incompativel", message);
    }

    public static RecursoIncompativelException recursoIncompativel(String message) {
        return new RecursoIncompativelException(message);
    }
}
