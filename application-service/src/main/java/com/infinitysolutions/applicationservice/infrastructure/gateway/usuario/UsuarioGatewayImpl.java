package com.infinitysolutions.applicationservice.infrastructure.gateway.usuario;

import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;
import com.infinitysolutions.applicationservice.core.port.UsuarioGateway;
import com.infinitysolutions.applicationservice.infrastructure.mapper.UsuarioEntityMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UsuarioGatewayImpl implements UsuarioGateway {

    private final UsuarioRepository repository;
    private final UsuarioEntityMapper usuarioMapper;

    @Override
    public Optional<Usuario> findUserById(UUID id) {
        return repository.findByIdAndIsAtivoTrue(id).map(usuarioMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public List<Usuario> findAll() {
        return repository.findAllByIsAtivoTrue().stream().map(usuarioMapper::toDomain).toList();
    }

}
