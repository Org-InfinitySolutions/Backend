package com.infinitysolutions.applicationservice.core.usecases.usuario;

import com.infinitysolutions.applicationservice.core.domain.usuario.Cargo;
import com.infinitysolutions.applicationservice.core.domain.usuario.Credencial;
import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;
import com.infinitysolutions.applicationservice.core.domain.valueobject.NomeCargo;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoUsuario;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoProcessavelException;
import com.infinitysolutions.applicationservice.core.gateway.CredenciaisGateway;
import com.infinitysolutions.applicationservice.core.usecases.cargo.ObterCargo;
import com.infinitysolutions.applicationservice.core.usecases.credencial.BuscarCredencialPorId;

import java.util.UUID;

public class RebaixarUsuario {

    private final BuscarCredencialPorId buscarCredencialPorId;
    private final BuscarUsuarioPorId buscarUsuarioPorId;
    private final ObterCargo obterCargo;
    private final CredenciaisGateway credenciaisGateway;

    public RebaixarUsuario(BuscarCredencialPorId buscarCredencialPorId, BuscarUsuarioPorId buscarUsuarioPorId, ObterCargo obterCargo, CredenciaisGateway credenciaisGateway) {
        this.buscarCredencialPorId = buscarCredencialPorId;
        this.buscarUsuarioPorId = buscarUsuarioPorId;
        this.obterCargo = obterCargo;
        this.credenciaisGateway = credenciaisGateway;
    }

    public void execute(UUID id) {
        Usuario usuarioEncontrado = buscarUsuarioPorId.execute(id);
        if (usuarioEncontrado.getTipoUsuario().equals(TipoUsuario.PJ)) throw RecursoNaoProcessavelException.usuarioComTipoIncorreto(usuarioEncontrado.getTipoUsuario());

        Credencial credencialEncontrada = buscarCredencialPorId.execute(id);
        Cargo cargoFuncionario = obterCargo.execute(NomeCargo.FUNCIONARIO);

        if (!credencialEncontrada.getCargos().contains(cargoFuncionario)) throw RecursoNaoProcessavelException.credencialNaoPossuiCargo(cargoFuncionario);
        credencialEncontrada.removeCargo(cargoFuncionario);
        credenciaisGateway.save(credencialEncontrada);
    }

}
