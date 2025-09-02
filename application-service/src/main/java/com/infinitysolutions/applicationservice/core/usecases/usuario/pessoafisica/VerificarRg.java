package com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Rg;
import com.infinitysolutions.applicationservice.core.gateway.PessoaFisicaGateway;
import com.infinitysolutions.applicationservice.core.usecases.usuario.RespostaVerificacao;

public class VerificarRg {

    private final PessoaFisicaGateway pessoaFisicaGateway;

    public VerificarRg(PessoaFisicaGateway pessoaFisicaGateway) {
        this.pessoaFisicaGateway = pessoaFisicaGateway;
    }

    public RespostaVerificacao execute(String rg) {
        Rg rg1 = Rg.of(rg);
        return new RespostaVerificacao(
                rg1.getValorFormatado(),
                !pessoaFisicaGateway.existsByRg(rg1.getValor())
        );
    }

}
