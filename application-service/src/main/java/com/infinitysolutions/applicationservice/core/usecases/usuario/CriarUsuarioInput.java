package com.infinitysolutions.applicationservice.core.usecases.usuario;

import com.infinitysolutions.applicationservice.core.usecases.endereco.EnderecoInput;

public abstract class CriarUsuarioInput {

    public String nome;
    public String telefoneCelular;
    public String tipo;
    public String email;
    public String senha;

    public EnderecoInput endereco;

    protected CriarUsuarioInput(String nome, String telefoneCelular, String tipo, String email, String senha, EnderecoInput endereco) {
        this.nome = nome;
        this.telefoneCelular = telefoneCelular;
        this.tipo = tipo;
        this.email = email;
        this.senha = senha;
        this.endereco = endereco;
    }
}
