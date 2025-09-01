package com.infinitysolutions.applicationservice.core.port;

import com.infinitysolutions.applicationservice.core.domain.Endereco;

import java.util.Optional;

public interface EnderecoGateway {
    Optional<Endereco> findByLogradouroAndComplemento(String logradouroNormalizado, String numeroNormalizado, String bairroNormalizado, String cidadeNormalizada, String estadoNormalizado, String cepNormalizado, String complementoNormalizado);

    Optional<Endereco> findByLogradouroAndComplementoIsNull(String logradouroNormalizado, String numeroNormalizado, String bairroNormalizado, String cidadeNormalizada, String estadoNormalizado, String cepNormalizado);

    Endereco save(Endereco novoEndereco);
}
