package com.infinitysolutions.applicationservice.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoNaoEncontradoException extends ApplicationServiceException{
    public RecursoNaoEncontradoException(String message) {
        super("recurso_nao_encontrado", message);
    }

    public static RecursoNaoEncontradoException pedidoNaoEncontrado(Integer id){
        return new RecursoNaoEncontradoException("Pedido não encontrado com o ID: " + id);
    }

    public static RecursoNaoEncontradoException pedidoNaoEncontradoComUsuario(Integer id, UUID userId){
        return new RecursoNaoEncontradoException("O usuário de ID " + userId.toString() + " não possui um Pedido de ID " + id);
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
    
    public static RecursoNaoEncontradoException credencialNaoEncontrada(UUID idUsuario) {
        return new RecursoNaoEncontradoException("Credencial não encontrada para usuário com ID: " + idUsuario);
    }

    public static RecursoNaoEncontradoException credencialNaoEncontrada(String email) {
        return new RecursoNaoEncontradoException("Credencial não encontrada para usuário com email: " + email);
    }
}
