package com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica;

import com.infinitysolutions.applicationservice.core.usecases.endereco.EnderecoInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.AtualizarUsuarioInput;

public class AtualizarPessoaJuridicaInput extends AtualizarUsuarioInput {
    private String razaoSocial;
    private String telefoneResidencial;

    public AtualizarPessoaJuridicaInput(String nome, String telefoneCelular, EnderecoInput endereco, String razaoSocial, String telefoneResidencial) {
        super(nome, telefoneCelular, endereco);
        this.razaoSocial = razaoSocial;
        this.telefoneResidencial = telefoneResidencial;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getTelefoneResidencial() {
        return telefoneResidencial;
    }

    public void setTelefoneResidencial(String telefoneResidencial) {
        this.telefoneResidencial = telefoneResidencial;
    }
}
