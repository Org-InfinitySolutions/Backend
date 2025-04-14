package com.infinitysolutions.authservice.repository;

import com.infinitysolutions.authservice.model.Cargo;
import com.infinitysolutions.authservice.model.enums.NomeCargo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CargoRepository extends JpaRepository<Cargo, Integer> {
    boolean existsByNome(NomeCargo nome);
    Optional<Cargo> findByNome(NomeCargo nome);
}
