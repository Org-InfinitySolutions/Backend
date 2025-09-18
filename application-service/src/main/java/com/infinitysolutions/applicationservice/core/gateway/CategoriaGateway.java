package com.infinitysolutions.applicationservice.core.gateway;

import com.infinitysolutions.applicationservice.core.domain.produto.Categoria;

import java.util.Optional;

public interface CategoriaGateway {
    Optional<Categoria> findCategoriaById(Integer id);
}
