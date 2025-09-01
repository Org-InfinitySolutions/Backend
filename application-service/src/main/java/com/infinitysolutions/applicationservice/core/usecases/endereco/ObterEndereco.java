package com.infinitysolutions.applicationservice.core.usecases.endereco;

import com.infinitysolutions.applicationservice.core.domain.Endereco;
import com.infinitysolutions.applicationservice.core.port.EnderecoGateway;

import java.util.Optional;

public class ObterEndereco {

    private final EnderecoGateway enderecoGateway;

    public ObterEndereco(EnderecoGateway enderecoGateway) {
        this.enderecoGateway = enderecoGateway;
    }

    public Endereco execute(EnderecoInput input) {
        String cepNormalizado = normalizarCEP(input.cep());
        String logradouroNormalizado = input.logradouro().trim();
        String bairroNormalizado = input.bairro().trim();
        String cidadeNormalizada = input.cidade().trim();
        String estadoNormalizado = input.estado().trim().toUpperCase();
        String numeroNormalizado = input.numero().trim();
        String complementoNormalizado = input.complemento() != null ? input.complemento().trim() : null;

        Optional<Endereco> enderecoExistente;

        if (complementoNormalizado != null && !complementoNormalizado.isBlank()) {
            enderecoExistente = enderecoGateway.findByLogradouroAndComplemento(
                    logradouroNormalizado,
                    numeroNormalizado,
                    bairroNormalizado,
                    cidadeNormalizada,
                    estadoNormalizado,
                    cepNormalizado,
                    complementoNormalizado
            );
        } else {
            enderecoExistente = enderecoGateway.findByLogradouroAndComplementoIsNull(
                    logradouroNormalizado,
                    numeroNormalizado,
                    bairroNormalizado,
                    cidadeNormalizada,
                    estadoNormalizado,
                    cepNormalizado
            );
        }
        if (enderecoExistente.isPresent()) return enderecoExistente.get();

        Endereco novoEndereco = new Endereco(
                null,
                cepNormalizado,
                logradouroNormalizado,
                bairroNormalizado,
                cidadeNormalizada,
                estadoNormalizado,
                numeroNormalizado,
                complementoNormalizado
        );

        return enderecoGateway.save(novoEndereco);
    }

    private String normalizarCEP(String cep) {
        return cep.replaceAll("[^0-9]", "");
    }

}
