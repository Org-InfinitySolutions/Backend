package com.infinitysolutions.crud.model.dto;

public record UsuarioDtoCriacao(
        String nomeCompleto,
        String cpf,
        String rg,
        String email,
        String telefone,
        String nomeFantasia,
        String razaoSocial,
        String cnpj
){}