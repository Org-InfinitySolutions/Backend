package com.infinitysolutions.applicationservice.core.usecases.usuario;

import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.gateway.ArquivoMetadadoGateway;
import com.infinitysolutions.applicationservice.core.gateway.UsuarioGateway;

import java.util.Optional;
import java.util.UUID;

public class BuscarUsuarioPorId {

    private final UsuarioGateway usuarioGateway;

    public BuscarUsuarioPorId(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }


    public Usuario execute(UUID id) {
        Optional<Usuario> usuarioOpt = usuarioGateway.findUserById(id);
        if (usuarioOpt.isEmpty()) throw RecursoNaoEncontradoException.usuarioNaoEncontrado(id);
        return usuarioOpt.get();
    }
}
