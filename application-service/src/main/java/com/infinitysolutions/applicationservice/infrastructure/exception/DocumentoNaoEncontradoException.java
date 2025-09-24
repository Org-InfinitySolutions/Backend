package com.infinitysolutions.applicationservice.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DocumentoNaoEncontradoException extends ApplicationServiceException {


    public DocumentoNaoEncontradoException(String message) {
        super("documento_nao_encontrado", message);
    }

    public static DocumentoNaoEncontradoException naoEncontradoPorNome(String nome){
        return new DocumentoNaoEncontradoException("Não foi encontrado um documento com o nome " + nome);
    }

    public static DocumentoNaoEncontradoException naoEncontradoPorId(Long id){
        return new DocumentoNaoEncontradoException("Não foi encontrado um documento com o id " + id);
    }
}
