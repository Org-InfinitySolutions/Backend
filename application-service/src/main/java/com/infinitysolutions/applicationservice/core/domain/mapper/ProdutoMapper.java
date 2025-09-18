package com.infinitysolutions.applicationservice.core.domain.mapper;

import com.infinitysolutions.applicationservice.core.domain.produto.Categoria;
import com.infinitysolutions.applicationservice.core.domain.produto.Produto;
import com.infinitysolutions.applicationservice.core.usecases.produto.CriarProdutoInput;

public class ProdutoMapper {

    public static Produto toDomain(CriarProdutoInput input, Categoria categoria) {
        return new Produto(
                input.modelo(),
                input.marca(),
                input.urlFabricante(),
                input.descricao(),
                input.qtdEstoque(),
                categoria
        );
    }
}
