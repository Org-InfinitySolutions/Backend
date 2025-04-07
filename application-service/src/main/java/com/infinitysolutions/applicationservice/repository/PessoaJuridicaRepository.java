package com.infinitysolutions.applicationservice.repository;

import com.infinitysolutions.applicationservice.model.PessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, UUID> {
    boolean existsByCnpj(String cnpj);
}
