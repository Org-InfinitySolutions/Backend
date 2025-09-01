package com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica;

import com.infinitysolutions.applicationservice.core.domain.Endereco;
import com.infinitysolutions.applicationservice.core.domain.PessoaFisica;
import com.infinitysolutions.applicationservice.core.domain.mapper.UsuarioMapper;
import com.infinitysolutions.applicationservice.core.port.PessoaFisicaGateway;
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

        boolean existePorCpf = pessoaFisicaGateway.existsByCpf(input.cpf.replaceAll("[.\\-\\s]", ""));
        boolean existePorRg = pessoaFisicaGateway.existsByRg(input.rg);

        if (existePorCpf) throw RecursoExistenteException.cpfJaEmUso(input.cpf);

        if (existePorRg) throw RecursoExistenteException.rgJaEmUso(input.rg);

        Endereco enderecoEncontrado = obterEndereco.execute(input.endereco);
        PessoaFisica novoUsuario = UsuarioMapper.toPessoaFisica(input, enderecoEncontrado);

        return pessoaFisicaGateway.save(novoUsuario);
    }

}
