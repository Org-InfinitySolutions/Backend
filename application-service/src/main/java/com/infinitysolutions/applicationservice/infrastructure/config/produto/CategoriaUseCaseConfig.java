package com.infinitysolutions.applicationservice.infrastructure.config.produto;

import com.infinitysolutions.applicationservice.core.gateway.CategoriaGateway;
import com.infinitysolutions.applicationservice.core.usecases.produto.categoria.BuscarCategoriaPorId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoriaUseCaseConfig {

    private final CategoriaGateway categoriaGateway;

    public CategoriaUseCaseConfig(CategoriaGateway categoriaGateway) {
        this.categoriaGateway = categoriaGateway;
    }

    @Bean
    public BuscarCategoriaPorId buscarCategoriaPorId() {
        return new BuscarCategoriaPorId(categoriaGateway);
    }
}
