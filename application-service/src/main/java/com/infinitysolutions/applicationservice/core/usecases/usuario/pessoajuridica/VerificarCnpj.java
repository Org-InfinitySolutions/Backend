package com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Cnpj;
import com.infinitysolutions.applicationservice.core.port.PessoaJuridicaGateway;
import com.infinitysolutions.applicationservice.core.usecases.usuario.RespostaVerificacao;

public class VerificarCnpj {

    private final PessoaJuridicaGateway pessoaJuridicaGateway;

    public VerificarCnpj(PessoaJuridicaGateway pessoaJuridicaGateway) {
        this.pessoaJuridicaGateway = pessoaJuridicaGateway;
    }

    public RespostaVerificacao execute(String cnpj) {
        Cnpj cnpj1 = Cnpj.of(cnpj);
        return new RespostaVerificacao(
                cnpj1.getValorFormatado(),
                !pessoaJuridicaGateway.existsByCnpj(cnpj1.getValor())
        );
    }
}
