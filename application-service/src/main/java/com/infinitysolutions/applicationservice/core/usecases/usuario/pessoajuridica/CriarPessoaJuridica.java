package com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica;

import com.infinitysolutions.applicationservice.core.domain.Endereco;
import com.infinitysolutions.applicationservice.core.domain.PessoaJuridica;
import com.infinitysolutions.applicationservice.core.domain.mapper.UsuarioMapper;
import com.infinitysolutions.applicationservice.core.port.PessoaJuridicaGateway;
import com.infinitysolutions.applicationservice.core.usecases.endereco.ObterEndereco;
import com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException;

public class CriarPessoaJuridica {

    private final PessoaJuridicaGateway pessoaJuridicaGateway;
    private final ObterEndereco obterEndereco;

    public CriarPessoaJuridica(PessoaJuridicaGateway pessoaJuridicaGateway, ObterEndereco obterEndereco) {
        this.pessoaJuridicaGateway = pessoaJuridicaGateway;
        this.obterEndereco = obterEndereco;
    }

    public PessoaJuridica execute(CriarPJInput input) {
        boolean existePorCnpj = pessoaJuridicaGateway.existsByCnpj(input.cnpj.replaceAll("[.\\-/\\\\s]", ""));
        if (existePorCnpj) throw RecursoExistenteException.cnpjJaEmUso(input.cnpj);

        Endereco enderecoEncontrado = obterEndereco.execute(input.endereco);
        PessoaJuridica novoUsuario = UsuarioMapper.toPessoaJuridica(input, enderecoEncontrado);
        return pessoaJuridicaGateway.save(novoUsuario);
    }
}
