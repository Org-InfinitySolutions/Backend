package com.infinitysolutions.applicationservice.core.gateway;

import com.infinitysolutions.applicationservice.core.domain.usuario.PessoaJuridica;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PessoaJuridicaGateway {
    PessoaJuridica save(PessoaJuridica user);

    boolean existsByCnpj(String s);

    List<PessoaJuridica> findAll();

    Optional<PessoaJuridica> findById(UUID id);

    PessoaJuridica update(PessoaJuridica usuario);
}
