package com.infinitysolutions.applicationservice.infrastructure.config.produto;

import com.infinitysolutions.applicationservice.core.gateway.CategoriaGateway;
import com.infinitysolutions.applicationservice.core.usecases.produto.categoria.*;
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

    @Bean
    public AtualizarCategoria atualizarCategoria() {
        return new AtualizarCategoria(buscarCategoriaPorId(), categoriaGateway);
    }

    @Bean
    public CriarCategoria criarCategoria() {
        return new CriarCategoria(categoriaGateway);
    }

    @Bean
    public ExcluirCategoria excluirCategoria() {
        return new ExcluirCategoria(buscarCategoriaPorId(), categoriaGateway);
    }

    @Bean
    public ListarTodasCategorias listarTodasCategorias() {
        return new ListarTodasCategorias(categoriaGateway);
    }
}
