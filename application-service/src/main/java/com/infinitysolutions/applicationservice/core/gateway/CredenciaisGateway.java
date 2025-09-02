package com.infinitysolutions.applicationservice.core.gateway;

import com.infinitysolutions.applicationservice.core.domain.usuario.Credencial;

import java.util.Optional;
import java.util.UUID;

public interface CredenciaisGateway {

    boolean existsByEmail(String email);
    boolean existsByUserId(UUID id);
    Credencial save(Credencial novaCredencial);
    Optional<Credencial> findByUserId(UUID id);
}
