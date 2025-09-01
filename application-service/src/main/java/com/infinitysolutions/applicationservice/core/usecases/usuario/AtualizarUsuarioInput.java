package com.infinitysolutions.applicationservice.core.usecases.usuario;

import com.infinitysolutions.applicationservice.core.usecases.endereco.EnderecoInput;

public abstract class AtualizarUsuarioInput {
    private String nome;
    private String telefoneCelular;
    private EnderecoInput endereco;

    protected AtualizarUsuarioInput(String nome, String telefoneCelular, EnderecoInput endereco) {
        this.nome = nome;
        this.telefoneCelular = telefoneCelular;
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

    public EnderecoInput getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoInput endereco) {
        this.endereco = endereco;
    }
}
