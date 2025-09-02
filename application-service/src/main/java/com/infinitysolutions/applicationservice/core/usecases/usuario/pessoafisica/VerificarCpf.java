package com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Cpf;
import com.infinitysolutions.applicationservice.core.gateway.PessoaFisicaGateway;
import com.infinitysolutions.applicationservice.core.usecases.usuario.RespostaVerificacao;

public class VerificarCpf {

    private final PessoaFisicaGateway gateway;

    public VerificarCpf(PessoaFisicaGateway gateway) {
        this.gateway = gateway;
    }

    public RespostaVerificacao execute(String cpf) {
        Cpf cpf1 = Cpf.of(cpf);
        return new RespostaVerificacao(
                    cpf1.getValorFormatado(),
                    !gateway.existsByCpf(cpf1.getValor())
            );

    }
}
