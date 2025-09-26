package com.infinitysolutions.applicationservice.core.domain;

public class Endereco {

    private final Integer id;
    private final String cep;
    private final String logradouro;
    private final String bairro;
    private final String cidade;
    private final String estado;
    private final String numero;
    private final String complemento;

    public Endereco(Integer id, String cep, String logradouro, String bairro, String cidade, String estado, String numero, String complemento) {
        this.id = id;
        this.cep = cep;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.numero = numero;
        this.complemento = complemento;
    }

    public Integer getId() {
        return id;
    }

    public String getCep() {
        return cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getNumero() {
        return numero;
    }

    public String getComplemento() {
        return complemento;
    }
}
