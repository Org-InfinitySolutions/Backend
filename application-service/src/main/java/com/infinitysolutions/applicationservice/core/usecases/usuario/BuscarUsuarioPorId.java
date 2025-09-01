package com.infinitysolutions.applicationservice.core.usecases.usuario;

import com.infinitysolutions.applicationservice.core.domain.ArquivoMetadado;
import com.infinitysolutions.applicationservice.core.domain.Usuario;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.port.ArquivoMetadadoGateway;
import com.infinitysolutions.applicationservice.core.port.UsuarioGateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BuscarUsuarioPorId {

    private final UsuarioGateway usuarioGateway;
    private final ArquivoMetadadoGateway arquivoMetadadoGateway;

    public BuscarUsuarioPorId(UsuarioGateway usuarioGateway, ArquivoMetadadoGateway arquivoMetadadoGateway) {
        this.usuarioGateway = usuarioGateway;
        this.arquivoMetadadoGateway = arquivoMetadadoGateway;
    }


    public Usuario execute(UUID id) {
        Optional<Usuario> usuarioOpt = usuarioGateway.findUserById(id);
        if (usuarioOpt.isEmpty()) throw RecursoNaoEncontradoException.usuarioNaoEncontrado(id);

        Usuario usuario = usuarioOpt.get();
        List<ArquivoMetadado> documentosUsuario = arquivoMetadadoGateway.findAllByUsuarioId(usuario.getId());
        usuario.setCadastroCompleto(documentosUsuario);

        return usuario;
    }
}
