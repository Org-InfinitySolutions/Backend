package com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository;

import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, UUID> {
    Optional<UsuarioEntity> findByIdAndIsAtivoTrue(UUID id);
    Page<UsuarioEntity> findAllByIsAtivoTrue(Pageable pageable);
}
