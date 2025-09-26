package com.infinitysolutions.applicationservice.core.usecases.credencial;

import com.infinitysolutions.applicationservice.core.domain.usuario.Cargo;
import com.infinitysolutions.applicationservice.core.domain.usuario.Credencial;
import com.infinitysolutions.applicationservice.core.exception.CredencialException;
import com.infinitysolutions.applicationservice.core.gateway.CredenciaisGateway;
import com.infinitysolutions.applicationservice.core.usecases.cargo.ObterCargo;

public class CriarCredencial {

    private final CredenciaisGateway credenciaisGateway;
    private final ObterCargo obterCargo;

    public CriarCredencial(CredenciaisGateway credenciaisGateway, ObterCargo obterCargo) {
        this.credenciaisGateway = credenciaisGateway;
        this.obterCargo = obterCargo;
    }

    public Credencial execute(CriarCredencialInput input) {

        if (credenciaisGateway.existsByEmail(input.email().getValor())) throw CredencialException.emailExistente(input.email().getValor());
        if (credenciaisGateway.existsByUserId(input.idUsuario())) throw CredencialException.usuarioIdExistente(input.idUsuario().toString());

        Cargo cargoEncontrado = obterCargo.execute(input.tipoUsuario());

        Credencial novaCredencial = Credencial.of(input.idUsuario(), input.email().getValor(), input.senha().getValor());
        novaCredencial.addCargo(cargoEncontrado);
        return credenciaisGateway.save(novaCredencial);

    }
}
