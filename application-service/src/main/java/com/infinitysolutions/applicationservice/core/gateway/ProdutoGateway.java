package com.infinitysolutions.applicationservice.core.gateway;

import com.infinitysolutions.applicationservice.core.domain.produto.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoGateway {
    Produto save(Produto novoProduto);
    List<Produto> findAll(boolean isAdmin);
    Optional<Produto> findById(boolean isAdmin, Integer id);
    Produto update(Produto produtoEncontrado);
    void remove(Integer produtoId);
}
