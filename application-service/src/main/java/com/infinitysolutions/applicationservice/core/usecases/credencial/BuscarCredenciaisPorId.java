package com.infinitysolutions.applicationservice.core.usecases.credencial;

import com.infinitysolutions.applicationservice.core.domain.usuario.Credencial;
import com.infinitysolutions.applicationservice.core.exception.CredencialException;
import com.infinitysolutions.applicationservice.core.gateway.CredenciaisGateway;

import java.util.Optional;
import java.util.UUID;

public class BuscarCredenciaisPorId {

    private final CredenciaisGateway credenciaisGateway;

    public BuscarCredenciaisPorId(CredenciaisGateway credenciaisGateway) {
        this.credenciaisGateway = credenciaisGateway;
    }

    public Credencial execute(UUID id) {
        Optional<Credencial> credencialOpt = credenciaisGateway.findByUserId(id);
        return credencialOpt.orElseThrow(() -> CredencialException.credencialNaoEncontrada(id.toString()));
    }
}
