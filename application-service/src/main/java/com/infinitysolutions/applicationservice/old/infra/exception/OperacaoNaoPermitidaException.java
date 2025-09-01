package com.infinitysolutions.applicationservice.old.infra.exception;

public class OperacaoNaoPermitidaException extends RuntimeException {
    public OperacaoNaoPermitidaException(String message) {
        super(message);
    }

    public static OperacaoNaoPermitidaException cancelamentoPedido(Integer pedidoId) {
        return new OperacaoNaoPermitidaException(
                String.format("Não é possível cancelar o pedido %d. Apenas pedidos em análise podem ser cancelados.", pedidoId)
        );
    }
}
