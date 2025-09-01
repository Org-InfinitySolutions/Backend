package com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository;


import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.PessoaFisicaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PessoaFisicaRepository extends JpaRepository<PessoaFisicaEntity, UUID> {
    boolean existsByCpf(String cpf);
    boolean existsById(UUID id);
    boolean existsByRg(String rg);
    boolean existsByRgContaining(String rg);
    Optional<PessoaFisicaEntity> findByIdAndIsAtivoTrue(UUID uuid);
    List<PessoaFisicaEntity> findAllByIsAtivoTrue();
}
