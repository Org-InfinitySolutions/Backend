package com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository;

import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.PessoaJuridicaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridicaEntity, UUID> {
    boolean existsByCnpj(String cnpj);
    Optional<PessoaJuridicaEntity> findByIdAndIsAtivoTrue(UUID uuid);
    List<PessoaJuridicaEntity> findAllByIsAtivoTrue();
}
