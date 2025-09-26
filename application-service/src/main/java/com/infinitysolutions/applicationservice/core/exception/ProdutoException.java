package com.infinitysolutions.applicationservice.core.exception;

public class ProdutoException extends CoreLayerException {
    
    public ProdutoException(String message) {
        super(message);
    }
    
    public ProdutoException(String message, Throwable cause) {
        super(message, cause.getMessage());
    }
    
    public static ProdutoException modeloObrigatorio() {
        return new ProdutoException("O modelo do produto é obrigatório");
    }
    
    public static ProdutoException marcaObrigatoria() {
        return new ProdutoException("A marca do produto é obrigatória");
    }
    
    public static ProdutoException categoriaObrigatoria() {
        return new ProdutoException("A categoria do produto é obrigatória");
    }
    
    public static ProdutoException estoqueInvalido(int estoque) {
        return new ProdutoException("Quantidade de estoque inválida: " + estoque + ". Deve ser maior ou igual a zero");
    }
    
    public static ProdutoException estoqueInsuficiente(int estoqueAtual, int quantidadeSolicitada) {
        return new ProdutoException("Estoque insuficiente. Disponível: " + estoqueAtual + ", Solicitado: " + quantidadeSolicitada);
    }
    
    public static ProdutoException produtoInativo() {
        return new ProdutoException("Não é possível realizar operações com produto inativo");
    }
    
    public static ProdutoException urlFabricanteInvalida(String url) {
        return new ProdutoException("URL do fabricante inválida: " + url);
    }
    
    public static ProdutoException imagensExcedidas(int quantidadeAtual, int limite) {
        return new ProdutoException("Quantidade de imagens excedida. Atual: " + quantidadeAtual + ", Limite: " + limite);
    }
}
