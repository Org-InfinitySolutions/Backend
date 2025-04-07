package com.infinitysolutions.applicationservice.mapper;

import com.infinitysolutions.applicationservice.model.Endereco;

public class EnderecoMapper {
    public static Endereco toEndereco(String cep, String logradouro, String numero, String bairro, String cidade, String estado, String complemento) {
        return new Endereco(cep, logradouro, bairro, cidade, estado, numero, complemento);
    }
}
