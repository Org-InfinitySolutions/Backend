package com.infinitysolutions.applicationservice.core.usecases.credencial;

import com.infinitysolutions.applicationservice.core.domain.usuario.Credencial;
import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import com.infinitysolutions.applicationservice.core.exception.AutenticacaoException;

public class BuscarCredencial {

    private final BuscarCredencialPorEmail buscarCredencialPorEmail;

    public BuscarCredencial(BuscarCredencialPorEmail buscarCredencialPorEmail) {
        this.buscarCredencialPorEmail = buscarCredencialPorEmail;
    }

    public Credencial execute(Email email, String senha) {
        Credencial credencialEncontrada = buscarCredencialPorEmail.execute(email);
        if (!credencialEncontrada.validarSenha(senha)) throw AutenticacaoException.credenciaisInvalidas();
        return credencialEncontrada;
    }
}
