package com.infinitysolutions.applicationservice.core.usecases.produto;

import com.infinitysolutions.applicationservice.core.domain.produto.Categoria;
import com.infinitysolutions.applicationservice.core.domain.produto.Produto;
import com.infinitysolutions.applicationservice.core.gateway.ProdutoGateway;
import com.infinitysolutions.applicationservice.core.usecases.produto.categoria.BuscarCategoriaPorId;

public class AtualizarProduto {

    private final BuscarProdutoPorId buscarProdutoPorId;
    private final BuscarCategoriaPorId buscarCategoriaPorId;
    private final ProdutoGateway produtoGateway;

    public AtualizarProduto(BuscarProdutoPorId buscarProdutoPorId, BuscarCategoriaPorId buscarCategoriaPorId, ProdutoGateway produtoGateway) {
        this.buscarProdutoPorId = buscarProdutoPorId;
        this.buscarCategoriaPorId = buscarCategoriaPorId;
        this.produtoGateway = produtoGateway;
    }

    public Produto execute(Integer produtoId, AtualizarProdutoInput input) {
        Produto produtoEncontrado = buscarProdutoPorId.execute(true, produtoId);
        Categoria categoriaEncontrada = buscarCategoriaPorId.execute(input.categoriaId());
        produtoEncontrado.atualizarDados(input, categoriaEncontrada);
        return produtoGateway.update(produtoEncontrado);
    }
}
