package com.infinitysolutions.applicationservice.core.usecases.produto;

import com.infinitysolutions.applicationservice.core.domain.produto.Produto;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.gateway.ProdutoGateway;

import java.util.Optional;

public class BuscarProdutoPorId {

    private final ProdutoGateway produtoGateway;

    public BuscarProdutoPorId(ProdutoGateway produtoGateway) {
        this.produtoGateway = produtoGateway;
    }

    public Produto execute(boolean isAdmin, Integer id) {
        Optional<Produto> produtoOpt = produtoGateway.findById(isAdmin, id);
        if (produtoOpt.isEmpty()) throw RecursoNaoEncontradoException.produtoNaoEncontrado(id);
        return produtoOpt.get();
    }
}
