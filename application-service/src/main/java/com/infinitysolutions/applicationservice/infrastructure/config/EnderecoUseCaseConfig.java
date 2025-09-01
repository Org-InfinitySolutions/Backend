package com.infinitysolutions.applicationservice.infrastructure.config;

import com.infinitysolutions.applicationservice.core.usecases.endereco.ObterEndereco;
import com.infinitysolutions.applicationservice.infrastructure.gateway.EnderecoGatewayImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnderecoUseCaseConfig {
    private final EnderecoGatewayImpl enderecoGateway;

    public EnderecoUseCaseConfig(EnderecoGatewayImpl enderecoGateway) {
        this.enderecoGateway = enderecoGateway;
    }

    @Bean
    public ObterEndereco obterEndereco() {
        return new ObterEndereco(enderecoGateway);
    }
}
