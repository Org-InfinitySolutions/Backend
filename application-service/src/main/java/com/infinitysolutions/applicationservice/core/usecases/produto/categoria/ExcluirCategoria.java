package com.infinitysolutions.applicationservice.core.usecases.produto.categoria;

import com.infinitysolutions.applicationservice.core.domain.produto.Categoria;
import com.infinitysolutions.applicationservice.core.gateway.CategoriaGateway;

public class ExcluirCategoria {

    private final BuscarCategoriaPorId buscarCategoriaPorId;
    private final CategoriaGateway categoriaGateway;

    public ExcluirCategoria(BuscarCategoriaPorId buscarCategoriaPorId, CategoriaGateway categoriaGateway) {
        this.buscarCategoriaPorId = buscarCategoriaPorId;
        this.categoriaGateway = categoriaGateway;
    }

    public void execute(Integer categoriaId) {
        Categoria categoriaEncontrada = buscarCategoriaPorId.execute(categoriaId);
        categoriaEncontrada.desativar();
        categoriaGateway.update(categoriaEncontrada);
    }
}
