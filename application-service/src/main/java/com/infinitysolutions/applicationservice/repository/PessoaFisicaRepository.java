package com.infinitysolutions.applicationservice.repository;


import com.infinitysolutions.applicationservice.model.PessoaFisica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, UUID> {
    boolean existsByCpf(String cpf);
    boolean existsById(UUID id);
    boolean existsByRgContaining(String rg);
    Optional<PessoaFisica> findByIdAndUsuario_IsAtivoTrue(UUID uuid);
    List<PessoaFisica> findAllByUsuario_IsAtivoTrue();
}
