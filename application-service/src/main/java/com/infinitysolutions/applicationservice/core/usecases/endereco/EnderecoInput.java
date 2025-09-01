package com.infinitysolutions.applicationservice.core.usecases.endereco;

public record EnderecoInput (
    String cep,
    String logradouro,
    String bairro,
    String cidade,
    String estado,
    String numero,
    String complemento
){}
