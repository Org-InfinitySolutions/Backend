package com.infinitysolutions.applicationservice.infrastructure.config.cargo;

import com.infinitysolutions.applicationservice.core.usecases.cargo.ObterCargo;
import com.infinitysolutions.applicationservice.infrastructure.gateway.cargo.CargoGatewayImpl;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CargoUseCaseConfig {

    private final CargoGatewayImpl cargoGateway;

    public CargoUseCaseConfig(CargoGatewayImpl cargoGateway) {
        this.cargoGateway = cargoGateway;
    }

    @Bean
    @Transactional
    public ObterCargo obterCargo() {
        return new ObterCargo(cargoGateway);
    }

}
