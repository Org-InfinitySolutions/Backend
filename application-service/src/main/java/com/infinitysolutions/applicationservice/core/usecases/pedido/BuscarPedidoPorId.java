package com.infinitysolutions.applicationservice.core.usecases.pedido;

import com.infinitysolutions.applicationservice.core.domain.pedido.Pedido;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.gateway.PedidoGateway;

import java.util.UUID;

public class BuscarPedidoPorId {

    private final PedidoGateway pedidoGateway;

    public BuscarPedidoPorId(PedidoGateway pedidoGateway) {
        this.pedidoGateway = pedidoGateway;
    }

    public Pedido execute(Integer pedidoId, UUID idUsuario, boolean isAdmin) {
        return pedidoGateway.findById(pedidoId, idUsuario, isAdmin).orElseThrow(() -> RecursoNaoEncontradoException.pedidoNaoEncontrado(pedidoId));
    }
}
