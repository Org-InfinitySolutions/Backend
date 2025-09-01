package com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica;

import com.infinitysolutions.applicationservice.core.usecases.usuario.CriarUsuarioInput;
import com.infinitysolutions.applicationservice.core.usecases.endereco.EnderecoInput;

public class CriarPFInput extends CriarUsuarioInput {
        public String cpf;
        public String rg;

        public CriarPFInput(String nome, String telefoneCelular, String tipo, String email, String senha, EnderecoInput endereco, String cpf, String rg) {
                super(nome, telefoneCelular, tipo, email, senha, endereco);
                this.cpf = cpf;
                this.rg = rg;
        }
}
