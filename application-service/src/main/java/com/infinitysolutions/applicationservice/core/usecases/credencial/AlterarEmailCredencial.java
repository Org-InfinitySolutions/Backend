package com.infinitysolutions.applicationservice.core.usecases.credencial;

import com.infinitysolutions.applicationservice.core.domain.usuario.Credencial;
import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import com.infinitysolutions.applicationservice.core.exception.AutenticacaoException;
import com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException;
import com.infinitysolutions.applicationservice.core.gateway.CredenciaisGateway;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailConfirmacaoResetEmail;

import java.util.UUID;

public class AlterarEmailCredencial {

    private final CredenciaisGateway credenciaisGateway;
    private final BuscarCredencialPorId buscarCredencialPorId;
    private final EnviarEmailConfirmacaoResetEmail enviarEmailConfirmacaoResetEmail;

    public AlterarEmailCredencial(CredenciaisGateway credenciaisGateway, BuscarCredencialPorId buscarCredencialPorId, EnviarEmailConfirmacaoResetEmail enviarEmailConfirmacaoResetEmail) {
        this.credenciaisGateway = credenciaisGateway;
        this.buscarCredencialPorId = buscarCredencialPorId;
        this.enviarEmailConfirmacaoResetEmail = enviarEmailConfirmacaoResetEmail;
    }

    public void execute(UUID usuarioId, String senha, String novoEmail) {

        if (credenciaisGateway.existsByEmail(novoEmail)) throw RecursoExistenteException.emailJaEmUso(novoEmail);

        Credencial credencialEncontrada = buscarCredencialPorId.execute(usuarioId);

        if (!credencialEncontrada.validarSenha(senha)) throw AutenticacaoException.credenciaisInvalidas();

        credencialEncontrada.alterarEmail(novoEmail);
        credenciaisGateway.save(credencialEncontrada);
        enviarEmailConfirmacaoResetEmail.execute(Email.of(novoEmail));
    }
}
