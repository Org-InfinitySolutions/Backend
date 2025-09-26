package com.infinitysolutions.applicationservice.core.usecases.credencial;

import com.infinitysolutions.applicationservice.core.domain.usuario.Credencial;
import com.infinitysolutions.applicationservice.core.exception.AutenticacaoException;
import com.infinitysolutions.applicationservice.core.gateway.CredenciaisGateway;
import com.infinitysolutions.applicationservice.core.usecases.usuario.ExcluirUsuario;

import java.util.UUID;

public class DeletarCredencial {

    private final BuscarCredencialPorId buscarCredencialPorId;
    private final CredenciaisGateway credenciaisGateway;
    private final ExcluirUsuario excluirUsuario;

    public DeletarCredencial(BuscarCredencialPorId buscarCredencialPorId, CredenciaisGateway credenciaisGateway, ExcluirUsuario excluirUsuario) {
        this.buscarCredencialPorId = buscarCredencialPorId;
        this.credenciaisGateway = credenciaisGateway;
        this.excluirUsuario = excluirUsuario;
    }

    public void execute(UUID usuarioId, String senha) {
        Credencial credencialEncontrada = buscarCredencialPorId.execute(usuarioId);
        if(!credencialEncontrada.validarSenha(senha)) throw AutenticacaoException.credenciaisInvalidas();
        credencialEncontrada.desativar();
        credenciaisGateway.save(credencialEncontrada);
        excluirUsuario.execute(usuarioId);
    }
}
