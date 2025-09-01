package com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica;

import com.infinitysolutions.applicationservice.core.usecases.endereco.EnderecoInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.AtualizarUsuarioInput;

public class AtualizarPessoaFisicaInput extends AtualizarUsuarioInput {
    public AtualizarPessoaFisicaInput(String nome, String telefoneCelular, EnderecoInput endereco) {
        super(nome, telefoneCelular, endereco);
    }
}
