package com.infinitysolutions.applicationservice.core.exception;

import java.util.UUID;

public class RecursoNaoEncontradoException extends CoreLayerException {
    public RecursoNaoEncontradoException(String message) {
        super("recurso_nao_encontrado", message);
    }

    public static com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException pedidoNaoEncontrado(Integer id){
        return new com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException("Pedido não encontrado com o ID: " + id);
    }

    public static com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException pedidoNaoEncontradoComUsuario(Integer id, UUID userId){
        return new com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException("O usuário de ID " + userId.toString() + " não possui um Pedido de ID " + id);
    }

    public static com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException usuarioNaoEncontrado(UUID uuid){
        return new com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException("Usuário não encontrado com o ID: " + uuid);
    }

    public static com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException usuarioNaoEncontrado(String email){
        return new com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException("Usuário não encontrado com o email: " + email);
    }

    public static com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException produtoNaoEncontrado(Integer id){
        return new com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException("Produto não encontrado com o ID: " + id);
    }

    public static com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException estrategiaNaoEncontrada(String tipoUsuario){
        return new com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException("Estratégia não encontrada para o tipo de usuário: " + tipoUsuario);
    }

    public static com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException credencialNaoEncontrada(UUID idUsuario) {
        return new com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException("Credencial não encontrada para usuário com ID: " + idUsuario);
    }

    public static com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException credencialNaoEncontrada(String email) {
        return new com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException("Credencial não encontrada para usuário com email: " + email);
    }
}
