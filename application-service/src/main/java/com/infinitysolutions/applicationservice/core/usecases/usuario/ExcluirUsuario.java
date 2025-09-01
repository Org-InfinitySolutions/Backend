package com.infinitysolutions.applicationservice.core.usecases.usuario;

import com.infinitysolutions.applicationservice.core.domain.Usuario;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.port.UsuarioGateway;

import java.util.Optional;
import java.util.UUID;

public class ExcluirUsuario {
    private final UsuarioGateway usuarioGateway;

    public ExcluirUsuario(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public void execute(UUID id) {
        Optional<Usuario> usuarioOpt = usuarioGateway.findUserById(id);
        if (usuarioOpt.isEmpty()) throw RecursoNaoEncontradoException.usuarioNaoEncontrado(id);
        usuarioGateway.deleteById(id);
    }
}
