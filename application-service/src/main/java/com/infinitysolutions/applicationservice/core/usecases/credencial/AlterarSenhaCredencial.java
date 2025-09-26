package com.infinitysolutions.applicationservice.core.usecases.credencial;

import com.infinitysolutions.applicationservice.core.domain.usuario.Credencial;
import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import com.infinitysolutions.applicationservice.core.gateway.CredenciaisGateway;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailConfirmacaoResetSenha;
import com.infinitysolutions.applicationservice.core.exception.AutenticacaoException;

import java.util.UUID;

public class AlterarSenhaCredencial {

    private final BuscarCredencialPorId buscarCredencialPorId;
    private final BuscarCredencialPorEmail buscarCredencialPorEmail;
    private final CredenciaisGateway credenciaisGateway;
    private final EnviarEmailConfirmacaoResetSenha enviarEmailConfirmacaoResetSenha;

    public AlterarSenhaCredencial(BuscarCredencialPorId buscarCredencialPorId, BuscarCredencialPorEmail buscarCredencialPorEmail, CredenciaisGateway credenciaisGateway, EnviarEmailConfirmacaoResetSenha enviarEmailConfirmacaoResetSenha) {
        this.buscarCredencialPorId = buscarCredencialPorId;
        this.buscarCredencialPorEmail = buscarCredencialPorEmail;
        this.credenciaisGateway = credenciaisGateway;
        this.enviarEmailConfirmacaoResetSenha = enviarEmailConfirmacaoResetSenha;
    }

    public void execute(UUID usuarioId, String senhaAtual, String novaSenha) {
        Credencial credencialEncontrada = buscarCredencialPorId.execute(usuarioId);

        if (!credencialEncontrada.validarSenha(senhaAtual)) throw AutenticacaoException.credenciaisInvalidas();

        Credencial credencialComNovaSenha = Credencial.of(credencialEncontrada.getUsuarioId(), credencialEncontrada.getEmailValor(), novaSenha);
        credenciaisGateway.save(credencialComNovaSenha);
        enviarEmailConfirmacaoResetSenha.execute(credencialEncontrada.getEmail());
    }

    public void execute(String email, String novaSenha) {
        Credencial credencialEncontrada = buscarCredencialPorEmail.execute(Email.of(email));
        Credencial credencialComNovaSenha = Credencial.of(credencialEncontrada.getUsuarioId(), credencialEncontrada.getEmailValor(), novaSenha);
        credenciaisGateway.save(credencialComNovaSenha);
        enviarEmailConfirmacaoResetSenha.execute(Email.of(email));
    }
}
