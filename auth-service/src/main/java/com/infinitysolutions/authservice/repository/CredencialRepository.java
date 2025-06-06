package com.infinitysolutions.authservice.repository;

import com.infinitysolutions.authservice.model.Credencial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CredencialRepository extends JpaRepository<Credencial, UUID> {
    boolean existsByFkUsuarioAndAtivoTrue(UUID id);
    boolean existsByEmailAndAtivoTrue(String email);
    Optional<Credencial> findByFkUsuarioAndAtivoTrue(UUID id);
    Optional<Credencial> findByEmailAndAtivoTrue(String email);
}
