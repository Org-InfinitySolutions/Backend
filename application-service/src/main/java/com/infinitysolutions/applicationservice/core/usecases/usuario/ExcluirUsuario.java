package com.infinitysolutions.applicationservice.core.usecases.usuario;

import com.infinitysolutions.applicationservice.core.domain.usuario.PessoaFisica;
import com.infinitysolutions.applicationservice.core.domain.usuario.PessoaJuridica;
import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.gateway.PessoaFisicaGateway;
import com.infinitysolutions.applicationservice.core.gateway.PessoaJuridicaGateway;
import com.infinitysolutions.applicationservice.core.gateway.UsuarioGateway;

import java.util.Optional;
import java.util.UUID;

public class ExcluirUsuario {
    private final UsuarioGateway usuarioGateway;
    private final PessoaFisicaGateway pessoaFisicaGateway;
    private final PessoaJuridicaGateway pessoaJuridicaGateway;

    public ExcluirUsuario(UsuarioGateway usuarioGateway, PessoaFisicaGateway pessoaFisicaGateway, PessoaJuridicaGateway pessoaJuridicaGateway) {
        this.usuarioGateway = usuarioGateway;
        this.pessoaFisicaGateway = pessoaFisicaGateway;
        this.pessoaJuridicaGateway = pessoaJuridicaGateway;
    }

    public void execute(UUID id) {
        Optional<Usuario> usuarioOpt = usuarioGateway.findUserById(id);
        if (usuarioOpt.isEmpty()) throw RecursoNaoEncontradoException.usuarioNaoEncontrado(id);
        usuarioOpt.get().desativar();

        switch (usuarioOpt.get().getTipoUsuario()) {
            case PJ -> pessoaJuridicaGateway.update((PessoaJuridica) usuarioOpt.get());
            case PF -> pessoaFisicaGateway.update((PessoaFisica) usuarioOpt.get());
        }
    }
}
