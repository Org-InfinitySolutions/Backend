package com.infinitysolutions.applicationservice.core.usecases.produto.categoria;

import com.infinitysolutions.applicationservice.core.domain.produto.Categoria;
import com.infinitysolutions.applicationservice.core.gateway.CategoriaGateway;

import java.util.List;

public class ListarTodasCategorias {

    private final CategoriaGateway categoriaGateway;

    public ListarTodasCategorias(CategoriaGateway categoriaGateway) {
        this.categoriaGateway = categoriaGateway;
    }

    public List<Categoria> execute() {
        return categoriaGateway.findAll();
    }
}
