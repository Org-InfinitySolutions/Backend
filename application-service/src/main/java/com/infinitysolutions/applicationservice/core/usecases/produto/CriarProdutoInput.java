package com.infinitysolutions.applicationservice.core.usecases.produto;

public record CriarProdutoInput(
        String modelo,
        String marca,
        String urlFabricante,
        String descricao,
        Integer qtdEstoque,
        Integer categoriaId
) {
}
