package com.infinitysolutions.applicationservice.core.usecases.usuario;

import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;
import com.infinitysolutions.applicationservice.core.gateway.UsuarioGateway;
import com.infinitysolutions.applicationservice.core.valueobject.PageResult;

public class ListarTodosUsuarios {

    private final UsuarioGateway usuarioGateway;

    public ListarTodosUsuarios(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public PageResult<Usuario> execute(int offset, int limit) {
        return usuarioGateway.findAll(offset, limit);
    }
}
