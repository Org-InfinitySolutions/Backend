package com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.auth;

import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.CredencialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CredencialRepository extends JpaRepository<CredencialEntity, UUID> {
    boolean existsByFkUsuarioAndAtivoTrue(UUID id);
    boolean existsByEmailAndAtivoTrue(String email);
    Optional<CredencialEntity> findByFkUsuarioAndAtivoTrue(UUID id);
    Optional<CredencialEntity> findByEmailAndAtivoTrue(String email);
}
