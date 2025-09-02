package com.infinitysolutions.applicationservice.core.usecases.cargo;

import com.infinitysolutions.applicationservice.core.domain.usuario.Cargo;
import com.infinitysolutions.applicationservice.core.domain.valueobject.NomeCargo;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoUsuario;
import com.infinitysolutions.applicationservice.core.gateway.CargoGateway;

import java.util.Optional;

public class ObterCargo {

    private final CargoGateway cargoGateway;

    public ObterCargo(CargoGateway cargoGateway) {
        this.cargoGateway = cargoGateway;
    }

    public Cargo execute(TipoUsuario tipoUsuario) {

        NomeCargo nomeCargo = switch (tipoUsuario) {
            case PF -> NomeCargo.USUARIO_PF;
            case PJ -> NomeCargo.USUARIO_PJ;
        };

        Optional<Cargo> cargoOpt = cargoGateway.findByNome(nomeCargo);
        return cargoOpt.orElseGet(() -> cargoGateway.save(Cargo.criar(nomeCargo, nomeCargo.getDescricao())));

    }
}
