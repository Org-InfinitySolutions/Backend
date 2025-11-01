package com.infinitysolutions.applicationservice.core.usecases.produto;

import com.infinitysolutions.applicationservice.core.domain.produto.Produto;
import com.infinitysolutions.applicationservice.core.gateway.ProdutoGateway;
import com.infinitysolutions.applicationservice.core.valueobject.PageResult;

import java.util.List;

public class ListarTodosProdutos {

    private final ProdutoGateway produtoGateway;

    public ListarTodosProdutos(ProdutoGateway produtoGateway) {
        this.produtoGateway = produtoGateway;
    }

    public List<Produto> execute(boolean isAdmin) {
        return produtoGateway.findAll(isAdmin);
    }

    public PageResult<Produto> execute(boolean isAdmin, int offset, int limit) {
        return produtoGateway.findAllPaginated(isAdmin, offset, limit);
    }
}