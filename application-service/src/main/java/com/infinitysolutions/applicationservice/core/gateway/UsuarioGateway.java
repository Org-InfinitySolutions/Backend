package com.infinitysolutions.applicationservice.core.gateway;

import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioGateway {
    Optional<Usuario> findUserById(UUID id);

    List<Usuario> findAll();
}
