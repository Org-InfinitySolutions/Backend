package com.infinitysolutions.applicationservice.service;

import com.infinitysolutions.applicationservice.mapper.EnderecoMapper;
import com.infinitysolutions.applicationservice.model.Endereco;
import com.infinitysolutions.applicationservice.model.dto.EnderecoDTO;
import com.infinitysolutions.applicationservice.repository.EnderecoRepository;
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
    public Endereco buscarEndereco(EnderecoDTO dto) {
        log.info("Buscando endereço: {}", dto);

        String cepNormalizado = normalizarCEP(dto.getCep());
        String numeroNormalizado = dto.getNumero().trim();

        Optional<Endereco> enderecoExistente;

        if (dto.getComplemento() != null && !dto.getComplemento().isBlank()) {
            String complementoNormalizado = dto.getComplemento().trim();

            enderecoExistente = repository.findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIgnoreCase(
                    dto.getLogradouro(),
                    numeroNormalizado,
                    dto.getBairro(),
                    dto.getCidade(),
                    dto.getEstado(),
                    cepNormalizado,
                    complementoNormalizado
            );
        } else {
            enderecoExistente = repository.findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIsNull(
                    dto.getLogradouro(),
                    numeroNormalizado,
                    dto.getBairro(),
                    dto.getCidade(),
                    dto.getEstado(),
                    cepNormalizado
            );
        }

        if (enderecoExistente.isPresent()){
            log.info("Endereço encontrado: {}", enderecoExistente.get().getId());
            return enderecoExistente.get();
        }

        Endereco novoEndereco = EnderecoMapper.toEndereco(
                cepNormalizado, dto.getLogradouro(), numeroNormalizado,
                dto.getBairro(), dto.getCidade(), dto.getEstado(), dto.getComplemento());

        Endereco enderecoSalvo = repository.save(novoEndereco);
        log.info("Novo Endereço criado com o ID: {}", enderecoSalvo.getId());
        return enderecoSalvo;
    }

    private String normalizarCEP(String cep) {
        return cep.replaceAll("[^0-9]", "");
    }
}
