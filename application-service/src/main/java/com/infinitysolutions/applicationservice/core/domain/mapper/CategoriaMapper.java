package com.infinitysolutions.applicationservice.core.domain.mapper;

import com.infinitysolutions.applicationservice.core.domain.produto.Categoria;
import com.infinitysolutions.applicationservice.core.domain.produto.Produto;

import java.util.List;

public class CategoriaMapper {

    public static Categoria toCategoria(Integer id, String nome, boolean isAtivo, List<Produto> produtos) {
        return new Categoria(
                id,
                nome,
                isAtivo,
                produtos
        );
    }

}
