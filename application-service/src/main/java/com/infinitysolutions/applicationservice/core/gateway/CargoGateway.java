package com.infinitysolutions.applicationservice.core.gateway;

import com.infinitysolutions.applicationservice.core.domain.usuario.Cargo;
import com.infinitysolutions.applicationservice.core.domain.valueobject.NomeCargo;

import java.util.Optional;

public interface CargoGateway {

    boolean existsByNome(NomeCargo nomeCargo);
    Optional<Cargo> findByNome(NomeCargo nomeCargo);
    Cargo save(Cargo cargo);
}
