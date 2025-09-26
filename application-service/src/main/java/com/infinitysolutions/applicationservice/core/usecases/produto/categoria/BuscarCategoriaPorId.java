package com.infinitysolutions.applicationservice.core.usecases.produto.categoria;

import com.infinitysolutions.applicationservice.core.domain.produto.Categoria;
import com.infinitysolutions.applicationservice.core.exception.CategoriaException;
import com.infinitysolutions.applicationservice.core.gateway.CategoriaGateway;

import java.util.Optional;

public class BuscarCategoriaPorId {

    private final CategoriaGateway categoriaGateway;

    public BuscarCategoriaPorId(CategoriaGateway categoriaGateway) {
        this.categoriaGateway = categoriaGateway;
    }

    public Categoria execute(Integer id) {
        Optional<Categoria> categoriaOpt = categoriaGateway.findCategoriaById(id);
        if (categoriaOpt.isEmpty()) throw CategoriaException.categoriaNaoEncontrada(id);
        return categoriaOpt.get();
    }
}
