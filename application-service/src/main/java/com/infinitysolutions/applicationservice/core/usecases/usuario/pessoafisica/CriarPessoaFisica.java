package com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica;

import com.infinitysolutions.applicationservice.core.domain.Endereco;
import com.infinitysolutions.applicationservice.core.domain.usuario.PessoaFisica;
import com.infinitysolutions.applicationservice.core.domain.mapper.UsuarioMapper;
import com.infinitysolutions.applicationservice.core.gateway.PessoaFisicaGateway;
import com.infinitysolutions.applicationservice.core.usecases.endereco.ObterEndereco;
import com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException;

public class CriarPessoaFisica {

    private final PessoaFisicaGateway pessoaFisicaGateway;
    private final ObterEndereco obterEndereco;

    public CriarPessoaFisica(PessoaFisicaGateway pessoaFisicaGateway, ObterEndereco obterEndereco) {
        this.pessoaFisicaGateway = pessoaFisicaGateway;
        this.obterEndereco = obterEndereco;
    }

    public PessoaFisica execute(CriarPFInput input) {

        boolean existePorCpf = pessoaFisicaGateway.existsByCpf(input.cpf.getValor());
        boolean existePorRg = pessoaFisicaGateway.existsByRg(input.rg.getValor());

        if (existePorCpf) throw RecursoExistenteException.cpfJaEmUso(input.cpf.getValorFormatado());

        if (existePorRg) throw RecursoExistenteException.rgJaEmUso(input.rg.getValorFormatado());

        Endereco enderecoEncontrado = obterEndereco.execute(input.endereco);
        PessoaFisica novoUsuario = UsuarioMapper.toPessoaFisica(input, enderecoEncontrado);

        return pessoaFisicaGateway.save(novoUsuario);
    }

}
