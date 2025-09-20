package com.infinitysolutions.applicationservice.core.usecases.produto.categoria;

import com.infinitysolutions.applicationservice.core.domain.produto.Categoria;
import com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException;
import com.infinitysolutions.applicationservice.core.gateway.CategoriaGateway;

public class AtualizarCategoria {

    private final BuscarCategoriaPorId buscarCategoriaPorId;
    private final CategoriaGateway categoriaGateway;

    public AtualizarCategoria(BuscarCategoriaPorId buscarCategoriaPorId, CategoriaGateway categoriaGateway) {
        this.buscarCategoriaPorId = buscarCategoriaPorId;
        this.categoriaGateway = categoriaGateway;
    }

    public Categoria execute(Integer categoriaId, String nome) {
        Categoria categoriaEncontrada  = buscarCategoriaPorId.execute(categoriaId);

        if (!categoriaEncontrada.getNome().equalsIgnoreCase(nome) && categoriaGateway.existsByNome(nome)) throw new RecursoExistenteException("Já existe outra categoria com este nome: " + nome);
        categoriaEncontrada.setNome(nome);

        return categoriaGateway.update(categoriaEncontrada);
    }
}
