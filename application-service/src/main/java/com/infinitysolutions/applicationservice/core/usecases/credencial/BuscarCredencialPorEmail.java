package com.infinitysolutions.applicationservice.core.usecases.credencial;

import com.infinitysolutions.applicationservice.core.domain.usuario.Credencial;
import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import com.infinitysolutions.applicationservice.core.exception.AutenticacaoException;
import com.infinitysolutions.applicationservice.core.gateway.CredenciaisGateway;

import java.util.Optional;

public class BuscarCredencialPorEmail {

    private final CredenciaisGateway credenciaisGateway;

    public BuscarCredencialPorEmail(CredenciaisGateway credenciaisGateway) {
        this.credenciaisGateway = credenciaisGateway;
    }

    public Credencial execute(Email email) {
        Optional<Credencial> credencialOpt = credenciaisGateway.findByUserEmail(email);
        return credencialOpt.orElseThrow(AutenticacaoException::credenciaisInvalidas);
    }

}
