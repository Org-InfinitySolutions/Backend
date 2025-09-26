package com.infinitysolutions.applicationservice.core.usecases.produto;

import com.infinitysolutions.applicationservice.core.domain.produto.Produto;
import com.infinitysolutions.applicationservice.core.gateway.ProdutoGateway;

import java.util.Set;

public class BuscarProdutosPorIds {

    private final ProdutoGateway produtoGateway;

    public BuscarProdutosPorIds(ProdutoGateway produtoGateway) {
        this.produtoGateway = produtoGateway;
    }

    public Set<Produto> execute(Set<Integer> ids) {
        return produtoGateway.findAllByIds(ids);
    }
}
