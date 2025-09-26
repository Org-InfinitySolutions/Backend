package com.infinitysolutions.applicationservice.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class VinculoExistenteException extends ApplicationServiceException {
    
    public VinculoExistenteException(String message) {
        super("vinculo_existente", message);
    }
    
    public static VinculoExistenteException produtoVinculadoAPedidos(Integer produtoId) {
        return new VinculoExistenteException(
            String.format("Não é possível excluir o produto com ID %d pois ele está vinculado a um ou mais pedidos. " +
                         "Para excluir o produto, primeiro é necessário remover ou alterar os pedidos que o utilizam.", produtoId)
        );
    }
    
    public static VinculoExistenteException categoriaVinculadaAProdutos(Integer categoriaId) {
        return new VinculoExistenteException(
            String.format("Não é possível excluir a categoria com ID %d pois ela possui produtos vinculados. " +
                         "Para excluir a categoria, primeiro é necessário remover ou alterar a categoria dos produtos.", categoriaId)
        );
    }
}
