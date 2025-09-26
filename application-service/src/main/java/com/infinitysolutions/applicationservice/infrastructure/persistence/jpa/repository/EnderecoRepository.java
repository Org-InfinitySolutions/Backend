package com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository;

import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.EnderecoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<EnderecoEntity, Integer> {
    Optional<EnderecoEntity> findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIsNull(
            String logradouro,
            String numero,
            String bairro,
            String cidade,
            String estado,
            String cep
    );

    Optional<EnderecoEntity> findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIgnoreCase(
            String logradouro,
            String numero,
            String bairro,
            String cidade,
            String estado,
            String cep,
            String complemento
    );
}
