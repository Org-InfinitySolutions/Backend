package com.infinitysolutions.applicationservice.infrastructure.gateway.cargo;

import com.infinitysolutions.applicationservice.core.domain.usuario.Cargo;
import com.infinitysolutions.applicationservice.core.domain.valueobject.NomeCargo;
import com.infinitysolutions.applicationservice.core.gateway.CargoGateway;
import com.infinitysolutions.applicationservice.infrastructure.mapper.auth.CargoMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.auth.CargoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CargoGatewayImpl implements CargoGateway {

    private final CargoRepository cargoRepository;

    @Override
    public boolean existsByNome(NomeCargo nomeCargo) {
        return cargoRepository.existsByNome(nomeCargo);
    }

    @Override
    public Optional<Cargo> findByNome(NomeCargo nomeCargo) {
        return cargoRepository.findByNome(nomeCargo).map(CargoMapper::toDomain);
    }

    @Override
    public Cargo save(Cargo cargo) {
        return CargoMapper.toDomain(cargoRepository.save(CargoMapper.toEntity(cargo)));
    }
}
