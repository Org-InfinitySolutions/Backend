package com.infinitysolutions.applicationservice.infrastructure.mapper;

import com.infinitysolutions.applicationservice.core.domain.Endereco;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.EnderecoEntity;

public class EnderecoMapper {
    public static EnderecoEntity toEndereco(String cep, String logradouro, String numero, String bairro, String cidade, String estado, String complemento) {
        return new EnderecoEntity(cep, logradouro, bairro, cidade, estado, numero, complemento);
    }

    public static EnderecoEntity toEndereco(Integer id, String cep, String logradouro, String numero, String bairro, String cidade, String estado, String complemento) {
        return new EnderecoEntity(id, cep, logradouro, bairro, cidade, estado, numero, complemento);
    }

    public static Endereco toDomain(EnderecoEntity entity) {
        if (entity == null) return null;
        return new Endereco(
                entity.getId(),
                entity.getCep(),
                entity.getLogradouro(),
                entity.getBairro(),
                entity.getCidade(),
                entity.getEstado(),
                entity.getNumero(),
                entity.getComplemento()
        );
    }
}
