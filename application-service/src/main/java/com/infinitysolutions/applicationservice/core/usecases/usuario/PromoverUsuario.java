package com.infinitysolutions.applicationservice.core.usecases.usuario;

import com.infinitysolutions.applicationservice.core.domain.usuario.Cargo;
import com.infinitysolutions.applicationservice.core.domain.usuario.Credencial;
import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;
import com.infinitysolutions.applicationservice.core.domain.valueobject.NomeCargo;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoUsuario;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoProcessavelException;
import com.infinitysolutions.applicationservice.core.gateway.CredenciaisGateway;
import com.infinitysolutions.applicationservice.core.gateway.UsuarioGateway;
import com.infinitysolutions.applicationservice.core.usecases.cargo.ObterCargo;
import com.infinitysolutions.applicationservice.core.usecases.credencial.BuscarCredencialPorId;

import java.util.UUID;

public class PromoverUsuario {

    private final BuscarUsuarioPorId buscarUsuarioPorId;
    private final BuscarCredencialPorId buscarCredencialPorId;
    private final ObterCargo obterCargo;
    private final CredenciaisGateway credenciaisGateway;

    public PromoverUsuario(BuscarUsuarioPorId buscarUsuarioPorId, BuscarCredencialPorId buscarCredencialPorId, ObterCargo obterCargo, CredenciaisGateway credenciaisGateway) {
        this.buscarUsuarioPorId = buscarUsuarioPorId;
        this.buscarCredencialPorId = buscarCredencialPorId;
        this.obterCargo = obterCargo;
        this.credenciaisGateway = credenciaisGateway;
    }

    public void execute(UUID id ) {
        Usuario usuarioEncontrado = buscarUsuarioPorId.execute(id);
        if (usuarioEncontrado.getTipoUsuario().equals(TipoUsuario.PJ)) throw RecursoNaoProcessavelException.usuarioComTipoIncorreto(usuarioEncontrado.getTipoUsuario());

        Credencial credencialEncontrada = buscarCredencialPorId.execute(id);
        Cargo cargoFuncionario = obterCargo.execute(NomeCargo.FUNCIONARIO);

        if (credencialEncontrada.getCargos().contains(cargoFuncionario)) throw RecursoNaoProcessavelException.credencialJaPossuiCargo(cargoFuncionario);
        credencialEncontrada.addCargo(cargoFuncionario);
        credenciaisGateway.save(credencialEncontrada);
    }

}
