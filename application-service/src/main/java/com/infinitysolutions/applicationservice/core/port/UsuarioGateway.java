package com.infinitysolutions.applicationservice.core.port;

import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioGateway {
    Optional<Usuario> findUserById(UUID id);

    void deleteById(UUID id);

    List<Usuario> findAll();

}
