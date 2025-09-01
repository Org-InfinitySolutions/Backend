package com.infinitysolutions.applicationservice.core.usecases.usuario;

import com.infinitysolutions.applicationservice.core.domain.Usuario;
import com.infinitysolutions.applicationservice.core.port.UsuarioGateway;

import java.util.List;

public class ListarTodosUsuarios {

    private final UsuarioGateway usuarioGateway;

    public ListarTodosUsuarios(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public List<Usuario> execute() {
        return usuarioGateway.findAll();
    }
}
