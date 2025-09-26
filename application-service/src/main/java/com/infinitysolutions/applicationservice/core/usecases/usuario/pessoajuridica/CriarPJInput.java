package com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Cnpj;
import com.infinitysolutions.applicationservice.core.usecases.usuario.CriarUsuarioInput;
import com.infinitysolutions.applicationservice.core.usecases.endereco.EnderecoInput;

public class CriarPJInput extends CriarUsuarioInput {
    public Cnpj cnpj;
    public String razaoSocial;
    public String telefoneResidencial;

    public CriarPJInput(String nome, String telefoneCelular, String tipo, String email, String senha, EnderecoInput endereco, String cnpj, String razaoSocial, String telefoneResidencial) {
        super(nome, telefoneCelular, tipo, email, senha, endereco);
        this.cnpj = Cnpj.of(cnpj);
        this.razaoSocial = razaoSocial;
        this.telefoneResidencial = telefoneResidencial;
    }
}
