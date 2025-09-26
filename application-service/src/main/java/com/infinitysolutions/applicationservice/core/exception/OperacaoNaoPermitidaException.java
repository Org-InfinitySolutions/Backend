package com.infinitysolutions.applicationservice.core.exception;

import com.infinitysolutions.applicationservice.core.domain.valueobject.SituacaoPedido;

public class OperacaoNaoPermitidaException extends RuntimeException {
    public OperacaoNaoPermitidaException(String message) {
        super(message);
    }

    public static OperacaoNaoPermitidaException cancelamentoPedido(Integer pedidoId) {
        return new OperacaoNaoPermitidaException(
                String.format("Não é possível cancelar o pedido %d. Apenas pedidos em análise podem ser cancelados.", pedidoId)
        );
    }

    public static OperacaoNaoPermitidaException  statusIguais(SituacaoPedido situacaoPedido) {
        return new OperacaoNaoPermitidaException(
                "Não é possível mudar a situação do pedido: A situação atual ja é " + situacaoPedido.getNome()
        );
    }
}
