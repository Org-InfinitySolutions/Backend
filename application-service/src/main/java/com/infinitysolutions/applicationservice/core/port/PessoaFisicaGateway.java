package com.infinitysolutions.applicationservice.core.port;

import com.infinitysolutions.applicationservice.core.domain.usuario.PessoaFisica;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PessoaFisicaGateway {
    PessoaFisica save(PessoaFisica usuario);

    boolean existsByCpf(String cpf);

    boolean existsByRg(String rg);

    List<PessoaFisica> findAll();

    Optional<PessoaFisica> findById(UUID id);

    PessoaFisica update(PessoaFisica usuario);
}
