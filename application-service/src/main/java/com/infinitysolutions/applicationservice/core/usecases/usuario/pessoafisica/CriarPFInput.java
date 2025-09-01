package com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Cpf;
import com.infinitysolutions.applicationservice.core.domain.valueobject.Rg;
import com.infinitysolutions.applicationservice.core.usecases.usuario.CriarUsuarioInput;
import com.infinitysolutions.applicationservice.core.usecases.endereco.EnderecoInput;

public class CriarPFInput extends CriarUsuarioInput {
        public Cpf cpf;
        public Rg rg;

        public CriarPFInput(String nome, String telefoneCelular, String tipo, String email, String senha, EnderecoInput endereco, String cpf, String rg) {
                super(nome, telefoneCelular, tipo, email, senha, endereco);
                this.cpf = Cpf.of(cpf);
                this.rg = Rg.of(rg);
        }
}
