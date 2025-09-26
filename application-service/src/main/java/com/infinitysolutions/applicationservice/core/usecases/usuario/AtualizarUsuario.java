package com.infinitysolutions.applicationservice.core.usecases.usuario;

import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;
import com.infinitysolutions.applicationservice.core.exception.EstrategiaNaoEncontradaException;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.AtualizarPessoaFisica;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.AtualizarPessoaFisicaInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.AtualizarPessoaJuridica;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.AtualizarPessoaJuridicaInput;

import java.util.UUID;

public class AtualizarUsuario {

    private final AtualizarPessoaFisica atualizarPessoaFisica;
    private final AtualizarPessoaJuridica atualizarPessoaJuridica;

    public AtualizarUsuario(AtualizarPessoaFisica atualizarPessoaFisica, AtualizarPessoaJuridica atualizarPessoaJuridica) {
        this.atualizarPessoaFisica = atualizarPessoaFisica;
        this.atualizarPessoaJuridica = atualizarPessoaJuridica;
    }

    public Usuario execute(UUID id, AtualizarUsuarioInput input) {
        if (input instanceof AtualizarPessoaJuridicaInput atualizarPessoaJuridicaInput) {
            return atualizarPessoaJuridica.execute(id, atualizarPessoaJuridicaInput);
        } else if (input instanceof AtualizarPessoaFisicaInput atualizarPessoaFisicaInput) {
            return atualizarPessoaFisica.execute(id, atualizarPessoaFisicaInput);
        }
        throw new EstrategiaNaoEncontradaException("Não foram encontradas estratégias para atualizar o usuario");
    }
}
