package com.infinitysolutions.applicationservice.infrastructure.gateway;

import com.infinitysolutions.applicationservice.core.domain.Endereco;
import com.infinitysolutions.applicationservice.core.port.EnderecoGateway;
import com.infinitysolutions.applicationservice.infrastructure.mapper.EnderecoMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.EnderecoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.EnderecoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class EnderecoGatewayImpl implements EnderecoGateway {

    private final EnderecoRepository repository;

    @Override
    public Optional<Endereco> findByLogradouroAndComplemento(String logradouroNormalizado, String numeroNormalizado, String bairroNormalizado, String cidadeNormalizada, String estadoNormalizado, String cepNormalizado, String complementoNormalizado) {
       return repository.findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIgnoreCase(
                logradouroNormalizado,
                numeroNormalizado,
                bairroNormalizado,
                cidadeNormalizada,
                estadoNormalizado,
                cepNormalizado,
                complementoNormalizado
        ).map(EnderecoMapper::toDomain);
    }

    @Override
    public Optional<Endereco> findByLogradouroAndComplementoIsNull(String logradouroNormalizado, String numeroNormalizado, String bairroNormalizado, String cidadeNormalizada, String estadoNormalizado, String cepNormalizado) {
        return repository.findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIsNull(
                logradouroNormalizado,
                numeroNormalizado,
                bairroNormalizado,
                cidadeNormalizada,
                estadoNormalizado,
                cepNormalizado
        ).map(EnderecoMapper::toDomain);
    }

    @Override
    @Transactional
    public Endereco save(Endereco novoEndereco) {
        EnderecoEntity newEntity = EnderecoMapper.toEndereco(novoEndereco.getCep(), novoEndereco.getLogradouro(), novoEndereco.getNumero(), novoEndereco.getBairro(), novoEndereco.getCidade(), novoEndereco.getEstado(), novoEndereco.getComplemento());
        EnderecoEntity savedEntity = repository.save(newEntity);
        return EnderecoMapper.toDomain(savedEntity);
    }
}
