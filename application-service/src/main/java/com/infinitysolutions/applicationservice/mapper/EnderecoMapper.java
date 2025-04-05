package com.infinitysolutions.applicationservice.mapper;

import com.infinitysolutions.applicationservice.model.Endereco;

public class EnderecoMapper {
    public static Endereco toEndereco(String logradouro, String numero, String bairro, String cidade, String estado, String cep, String complemento) {
        return new Endereco(logradouro, numero, bairro, cidade, estado, cep, complemento);
    }
}
