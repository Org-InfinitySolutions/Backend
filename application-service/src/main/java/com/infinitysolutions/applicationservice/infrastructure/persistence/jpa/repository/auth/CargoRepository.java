package com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.auth;

import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.CargoEntity;
import com.infinitysolutions.applicationservice.core.domain.valueobject.NomeCargo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CargoRepository extends JpaRepository<CargoEntity, Integer> {
    boolean existsByNome(NomeCargo nome);
    Optional<CargoEntity> findByNome(NomeCargo nome);
}
