package com.infinitysolutions.applicationservice.infrastructure.config;

import com.infinitysolutions.applicationservice.infrastructure.gateway.ArquivoMetadadoGatewayImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArquivoMetadadoUseCaseConfig {

    private final ArquivoMetadadoGatewayImpl arquivoMetadadoGatewayImpl;

    public ArquivoMetadadoUseCaseConfig(ArquivoMetadadoGatewayImpl arquivoMetadadoGatewayImpl) {
        this.arquivoMetadadoGatewayImpl = arquivoMetadadoGatewayImpl;
    }
}
