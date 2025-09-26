package com.infinitysolutions.applicationservice.core.gateway;

import com.infinitysolutions.applicationservice.core.domain.produto.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaGateway {
    Optional<Categoria> findCategoriaById(Integer id);
    List<Categoria> findAll();
    boolean existsByNome(String nomeCategoria);
    Categoria save(String nomeCategoria);
    Categoria update(Categoria categoriaEncontrada);
}
