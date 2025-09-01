package com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica;


import com.infinitysolutions.applicationservice.core.domain.usuario.PessoaJuridica;
import com.infinitysolutions.applicationservice.core.port.PessoaJuridicaGateway;

import java.util.List;

public class ListarPessoaJuridica {

    private final PessoaJuridicaGateway pessoaJuridicaGateway;

    public ListarPessoaJuridica(PessoaJuridicaGateway pessoaJuridicaGateway) {
        this.pessoaJuridicaGateway = pessoaJuridicaGateway;
    }

    public List<PessoaJuridica> execute() {
        return pessoaJuridicaGateway.findAll();
    }
}
