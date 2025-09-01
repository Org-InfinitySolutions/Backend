package com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository;

import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.ZipFile;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, UUID> {
    Optional<UsuarioEntity> findByIdAndIsAtivoTrue(UUID id);
    List<UsuarioEntity> findAllByIsAtivoTrue();
}