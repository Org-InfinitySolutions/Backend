package com.infinitysolutions.applicationservice.repository;

import com.infinitysolutions.applicationservice.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
    Optional<Endereco> findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIsNull(
            String logradouro,
            String numero,
            String bairro,
            String cidade,
            String estado,
            String cep
    );

    Optional<Endereco> findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIgnoreCase(
            String logradouro,
            String numero,
            String bairro,
            String cidade,
            String estado,
            String cep,
            String complemento
    );
}
