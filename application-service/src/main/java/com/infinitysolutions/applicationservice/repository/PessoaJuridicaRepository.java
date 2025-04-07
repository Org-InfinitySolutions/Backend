package com.infinitysolutions.applicationservice.repository;

import com.infinitysolutions.applicationservice.model.PessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, UUID> {
    boolean existsByCnpj(String cnpj);
    Optional<PessoaJuridica> findByIdAndUsuario_IsAtivoTrue(UUID uuid);
    List<PessoaJuridica> findAllByUsuario_IsAtivoTrue();
}
