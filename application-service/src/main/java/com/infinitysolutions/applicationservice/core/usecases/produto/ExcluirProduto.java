package com.infinitysolutions.applicationservice.core.usecases.produto;

import com.infinitysolutions.applicationservice.core.gateway.ProdutoGateway;

public class ExcluirProduto {
    private final ProdutoGateway produtoGateway;

    public ExcluirProduto(ProdutoGateway produtoGateway) {
        this.produtoGateway = produtoGateway;
    }

    public void execute(Integer produtoId) {
        produtoGateway.remove(produtoId);
    }
}
