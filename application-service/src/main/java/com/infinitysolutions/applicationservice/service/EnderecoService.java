package com.infinitysolutions.applicationservice.service;

import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.EnderecoEntity;
import com.infinitysolutions.applicationservice.infrastructure.mapper.EnderecoMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.endereco.EnderecoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnderecoService {

    private final EnderecoRepository repository;

    @Transactional
    public EnderecoEntity buscarEndereco(EnderecoDTO dto) {
        log.info("Buscando endereço: {}", dto);

        // Normalização de todos os campos para garantir consistência com o índice único
        String cepNormalizado = normalizarCEP(dto.getCep());
        String logradouroNormalizado = dto.getLogradouro().trim();
        String bairroNormalizado = dto.getBairro().trim();
        String cidadeNormalizada = dto.getCidade().trim();
        String estadoNormalizado = dto.getEstado().trim().toUpperCase();
        String numeroNormalizado = dto.getNumero().trim();
        String complementoNormalizado = dto.getComplemento() != null ? dto.getComplemento().trim() : null;

        Optional<EnderecoEntity> enderecoExistente;

        if (complementoNormalizado != null && !complementoNormalizado.isBlank()) {
            enderecoExistente = repository.findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIgnoreCase(
                    logradouroNormalizado,
                    numeroNormalizado,
                    bairroNormalizado,
                    cidadeNormalizada,
                    estadoNormalizado,
                    cepNormalizado,
                    complementoNormalizado
            );
        } else {
            enderecoExistente = repository.findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIsNull(
                    logradouroNormalizado,
                    numeroNormalizado,
                    bairroNormalizado,
                    cidadeNormalizada,
                    estadoNormalizado,
                    cepNormalizado
            );
        }

        if (enderecoExistente.isPresent()){
            log.info("Endereço encontrado: {}", enderecoExistente.get().getId());
            return enderecoExistente.get();
        }

        EnderecoEntity novoEnderecoEntity = EnderecoMapper.toEndereco(
                cepNormalizado, logradouroNormalizado, numeroNormalizado,
                bairroNormalizado, cidadeNormalizada, estadoNormalizado, 
                complementoNormalizado);

        try {
            EnderecoEntity enderecoEntitySalvo = repository.save(novoEnderecoEntity);
            log.info("Novo Endereço criado com o ID: {}", enderecoEntitySalvo.getId());
            return enderecoEntitySalvo;
        } catch (Exception e) {
            // Em caso de erro de constraint única (pode acontecer em ambiente concorrente)
            // Tentar buscar novamente o endereço
            log.warn("Erro ao salvar endereço, verificando se foi inserido concorrentemente: {}", e.getMessage());
            
            // Buscar novamente para caso o endereço tenha sido inserido por outro processo
            Optional<EnderecoEntity> enderecoInseridoConcorrentemente = complementoNormalizado != null && !complementoNormalizado.isBlank() ?
                repository.findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIgnoreCase(
                    logradouroNormalizado, numeroNormalizado, bairroNormalizado, cidadeNormalizada, 
                    estadoNormalizado, cepNormalizado, complementoNormalizado) :
                repository.findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIsNull(
                    logradouroNormalizado, numeroNormalizado, bairroNormalizado, cidadeNormalizada, 
                    estadoNormalizado, cepNormalizado);
            
            if (enderecoInseridoConcorrentemente.isPresent()) {
                log.info("Endereço encontrado após erro de concorrência: {}", enderecoInseridoConcorrentemente.get().getId());
                return enderecoInseridoConcorrentemente.get();
            }
            
            // Se ainda não encontrou, propaga o erro original
            throw e;
        }
    }

    private String normalizarCEP(String cep) {
        return cep.replaceAll("[^0-9]", "");
    }
}
