package com.infinitysolutions.applicationservice.infra.exception;

import com.infinitysolutions.applicationservice.model.enums.TipoAnexo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class DocumentoInvalidoException extends ApplicationServiceException {


    public DocumentoInvalidoException(String message) {
        super("documento_invalido", message);
    }

    public static DocumentoInvalidoException tipoAnexoInvalidoPorUsuario(TipoAnexo tipoAnexo, String tipoUsuario){
        return new DocumentoInvalidoException("O tipo_anexo " + tipoAnexo.toString() + " é inválido para o tipo de usuário " + tipoUsuario);
    }
}
