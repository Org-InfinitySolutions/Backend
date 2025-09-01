package com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica;

import com.infinitysolutions.applicationservice.core.domain.Endereco;
import com.infinitysolutions.applicationservice.core.domain.usuario.PessoaFisica;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.port.PessoaFisicaGateway;
import com.infinitysolutions.applicationservice.core.usecases.endereco.ObterEndereco;

import java.util.UUID;

public class AtualizarPessoaFisica {

    private final ObterEndereco obterEndereco;
    private final PessoaFisicaGateway pessoaFisicaGateway;

    public AtualizarPessoaFisica(ObterEndereco obterEndereco, PessoaFisicaGateway pessoaFisicaGateway) {
        this.obterEndereco = obterEndereco;
        this.pessoaFisicaGateway = pessoaFisicaGateway;
    }

    public PessoaFisica execute(UUID id, AtualizarPessoaFisicaInput input) {
        PessoaFisica usuarioEncontrado = pessoaFisicaGateway.findById(id).orElseThrow(() -> RecursoNaoEncontradoException.usuarioNaoEncontrado(id));
        Endereco endereco = obterEndereco.execute(input.getEndereco());
        usuarioEncontrado.atualizarDados(input, endereco);
        return pessoaFisicaGateway.update(usuarioEncontrado);
    }
}
