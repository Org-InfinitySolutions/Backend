package com.infinitysolutions.applicationservice.infrastructure.gateway.usuario;

import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;
import com.infinitysolutions.applicationservice.core.gateway.UsuarioGateway;
import com.infinitysolutions.applicationservice.core.valueobject.PageResult;
import com.infinitysolutions.applicationservice.infrastructure.mapper.UsuarioEntityMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.UsuarioEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public PageResult<Usuario> findAll(int offset, int limit) {
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);
        Page<UsuarioEntity> pageResult = repository.findAllByIsAtivoTrue(pageable);

        List<Usuario> usuarios = pageResult.getContent()
                .stream()
                .map(usuarioMapper::toDomain)
                .toList();

        return new PageResult<>(usuarios, pageResult.getTotalElements(), offset, limit);
    }
}
