package com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica;

import com.infinitysolutions.applicationservice.core.domain.Endereco;
import com.infinitysolutions.applicationservice.core.domain.PessoaJuridica;
import com.infinitysolutions.applicationservice.core.domain.Usuario;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.port.PessoaJuridicaGateway;
import com.infinitysolutions.applicationservice.core.usecases.endereco.ObterEndereco;

import java.util.UUID;

public class AtualizarPessoaJuridica {

    private final ObterEndereco obterEndereco;
    private final PessoaJuridicaGateway pessoaJuridicaGateway;

    public AtualizarPessoaJuridica(ObterEndereco obterEndereco, PessoaJuridicaGateway pessoaJuridicaGateway) {
        this.obterEndereco = obterEndereco;
        this.pessoaJuridicaGateway = pessoaJuridicaGateway;
    }

    public PessoaJuridica execute(UUID id, AtualizarPessoaJuridicaInput input) {
        PessoaJuridica usuarioEncontrado = pessoaJuridicaGateway.findById(id).orElseThrow(() -> RecursoNaoEncontradoException.usuarioNaoEncontrado(id));
        Endereco endereco = obterEndereco.execute(input.getEndereco());
        usuarioEncontrado.atualizarDados(input, endereco);
        return pessoaJuridicaGateway.update(usuarioEncontrado);
    }
}
