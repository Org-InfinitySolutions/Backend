package com.infinitysolutions.applicationservice.core.usecases.produto.categoria;

import com.infinitysolutions.applicationservice.core.domain.produto.Categoria;
import com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException;
import com.infinitysolutions.applicationservice.core.gateway.CategoriaGateway;

public class CriarCategoria {

    private final CategoriaGateway categoriaGateway;

    public CriarCategoria(CategoriaGateway categoriaGateway) {
        this.categoriaGateway = categoriaGateway;
    }

    public Categoria execute(String nomeCategoria) {
        if (categoriaGateway.existsByNome(nomeCategoria)) throw new RecursoExistenteException("Já existe uma categoria com este nome: " + nomeCategoria);
        return categoriaGateway.save(nomeCategoria);
    }
}
