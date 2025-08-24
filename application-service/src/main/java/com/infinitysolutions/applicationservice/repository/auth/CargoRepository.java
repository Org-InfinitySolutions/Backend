package com.infinitysolutions.applicationservice.repository.auth;

import com.infinitysolutions.applicationservice.model.auth.Cargo;
import com.infinitysolutions.applicationservice.model.auth.enums.NomeCargo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CargoRepository extends JpaRepository<Cargo, Integer> {
    boolean existsByNome(NomeCargo nome);
    Optional<Cargo> findByNome(NomeCargo nome);
}
