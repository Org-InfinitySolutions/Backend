package com.infinitysolutions.applicationservice.core.usecases.produto;

public record AtualizarProdutoInput(
    String modelo,
    String marca,
    String urlFabricante,
    String descricao,
    Integer qtdEstoque,
    Integer categoriaId,
    boolean isAtivo
){}
