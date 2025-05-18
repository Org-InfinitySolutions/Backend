package com.infinitysolutions.applicationservice.infra.exception;


import java.util.UUID;

public class RecursoNaoEncontradoException extends ApplicationServiceException{
    public RecursoNaoEncontradoException(String message) {

        super("recurso_nao_encontrado", message);
    }

    public static RecursoNaoEncontradoException usuarioNaoEncontrado(UUID uuid){
        return new RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + uuid);
    }

    public static RecursoNaoEncontradoException produtoNaoEncontrado(Integer id){
        return new RecursoNaoEncontradoException("Produto não encontrado com o ID: " + id);
    }

    public static RecursoNaoEncontradoException estrategiaNaoEncontrada(String tipoUsuario){
        return new RecursoNaoEncontradoException("Estratégia não encontrada para o tipo de usuário: " + tipoUsuario);
    }
}
