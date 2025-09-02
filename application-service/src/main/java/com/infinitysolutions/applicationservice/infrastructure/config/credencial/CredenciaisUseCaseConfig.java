package com.infinitysolutions.applicationservice.infrastructure.config.credencial;

import com.infinitysolutions.applicationservice.core.usecases.cargo.ObterCargo;
import com.infinitysolutions.applicationservice.core.usecases.credencial.BuscarCredenciaisPorId;
import com.infinitysolutions.applicationservice.core.usecases.credencial.CriarCredenciais;
import com.infinitysolutions.applicationservice.infrastructure.gateway.credencial.CredenciaisGatewayImpl;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CredenciaisUseCaseConfig {

    private final CredenciaisGatewayImpl credenciaisGateway;
    private final ObterCargo obterCargo;

    public CredenciaisUseCaseConfig(
            CredenciaisGatewayImpl credenciaisGateway,
            ObterCargo obterCargo
    ) {
        this.credenciaisGateway = credenciaisGateway;
        this.obterCargo = obterCargo;
    }

    @Bean
    @Transactional
    public CriarCredenciais criarCredenciais() {
        return new CriarCredenciais(credenciaisGateway, obterCargo);
    }

    @Bean
    public BuscarCredenciaisPorId buscarCredenciaisPorId() {
        return new BuscarCredenciaisPorId(credenciaisGateway);
    }
}
