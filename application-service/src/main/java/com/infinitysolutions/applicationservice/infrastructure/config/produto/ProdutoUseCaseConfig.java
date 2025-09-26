package com.infinitysolutions.applicationservice.infrastructure.config.produto;

import com.infinitysolutions.applicationservice.core.gateway.ProdutoGateway;
import com.infinitysolutions.applicationservice.core.usecases.produto.*;
import com.infinitysolutions.applicationservice.core.usecases.produto.categoria.BuscarCategoriaPorId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProdutoUseCaseConfig {

    private final BuscarCategoriaPorId buscarCategoriaPorId;
    private final AtualizarImagemProduto atualizarImagemProduto;
    private final ProdutoGateway produtoGateway;

    public ProdutoUseCaseConfig(BuscarCategoriaPorId buscarCategoriaPorId,
                                AtualizarImagemProduto atualizarImagemProduto,
                                ProdutoGateway produtoGateway
    ) {
        this.buscarCategoriaPorId = buscarCategoriaPorId;
        this.atualizarImagemProduto = atualizarImagemProduto;
        this.produtoGateway = produtoGateway;
    }

    @Bean
    public CriarProduto criarProduto() {
        return new CriarProduto(buscarCategoriaPorId, atualizarImagemProduto, produtoGateway);
    }

    @Bean
    public ListarTodosProdutos listarTodosProdutos() {
        return new ListarTodosProdutos(produtoGateway);
    }

    @Bean
    public BuscarProdutoPorId buscarProdutoPorId() {
        return new BuscarProdutoPorId(produtoGateway);
    }

    @Bean
    public AtualizarProduto atualizarProduto() {
        return new AtualizarProduto(buscarProdutoPorId(), buscarCategoriaPorId, produtoGateway);
    }

    @Bean
    public ExcluirProduto excluirProduto() {
        return new ExcluirProduto(produtoGateway);
    }

    @Bean
    public BuscarProdutosPorIds buscarProdutosPorIds() {
        return new BuscarProdutosPorIds(produtoGateway);
    }
}
