package com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica;

import com.infinitysolutions.applicationservice.core.domain.usuario.PessoaFisica;
import com.infinitysolutions.applicationservice.core.port.PessoaFisicaGateway;

import java.util.List;

public class ListarPessoaFisica {

    private final PessoaFisicaGateway pessoaFisicaGateway;

    public ListarPessoaFisica(PessoaFisicaGateway pessoaFisicaGateway) {
        this.pessoaFisicaGateway = pessoaFisicaGateway;
    }

    public List<PessoaFisica> execute() {
        return pessoaFisicaGateway.findAll();
    }
}
