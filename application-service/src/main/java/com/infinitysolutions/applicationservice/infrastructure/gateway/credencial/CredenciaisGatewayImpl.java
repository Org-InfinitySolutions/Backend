package com.infinitysolutions.applicationservice.infrastructure.gateway.credencial;

import com.infinitysolutions.applicationservice.core.domain.usuario.Credencial;
import com.infinitysolutions.applicationservice.core.gateway.CredenciaisGateway;
import com.infinitysolutions.applicationservice.infrastructure.mapper.auth.CredencialMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.CredencialEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.auth.CredencialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CredenciaisGatewayImpl implements CredenciaisGateway {

    private final CredencialRepository credencialRepository;

    @Override
    public boolean existsByEmail(String email) {
        return credencialRepository.existsByEmailAndAtivoTrue(email);
    }

    @Override
    public boolean existsByUserId(UUID id) {
        return credencialRepository.existsByFkUsuarioAndAtivoTrue(id);
    }

    @Override
    public Credencial save(Credencial novaCredencial) {
        CredencialEntity entity = CredencialMapper.toEntity(novaCredencial.getUsuarioId(), novaCredencial.getEmailValor(), novaCredencial.getHashSenha());
        return CredencialMapper.toCredencial(credencialRepository.save(entity));
    }

    @Override
    public Optional<Credencial> findByUserId(UUID id) {
        return credencialRepository.findByFkUsuarioAndAtivoTrue(id).map(CredencialMapper::toCredencial);
    }
}
