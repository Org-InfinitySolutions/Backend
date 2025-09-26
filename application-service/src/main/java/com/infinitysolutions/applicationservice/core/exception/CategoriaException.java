package com.infinitysolutions.applicationservice.core.exception;

public class CategoriaException extends CoreLayerException {
    
    public CategoriaException(String message) {
        super(message);
    }
    
    public CategoriaException(String message, Throwable cause) {
        super(message, cause.getMessage());
    }
    
    public static CategoriaException nomeInvalido(String nome) {
        return new CategoriaException("Nome da categoria inválido: " + nome);
    }
    
    public static CategoriaException nomeObrigatorio() {
        return new CategoriaException("O nome da categoria é obrigatório");
    }
    
    public static CategoriaException nomeMuitoLongo(int tamanhoMaximo) {
        return new CategoriaException("Nome da categoria excede o tamanho máximo permitido de " + tamanhoMaximo + " caracteres");
    }
    
    public static CategoriaException categoriaInativa() {
        return new CategoriaException("Não é possível realizar operações com categoria inativa");
    }

    public static CategoriaException categoriaNaoEncontrada(Integer id) {
        return new CategoriaException("Não foi encontrada uma categoria com o id: " + id);
    }
}
