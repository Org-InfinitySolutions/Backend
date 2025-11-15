package com.infinitysolutions.applicationservice.core.usecases.pedido;

import com.infinitysolutions.applicationservice.core.domain.pedido.Pedido;
import com.infinitysolutions.applicationservice.core.gateway.PedidoGateway;
import com.infinitysolutions.applicationservice.core.valueobject.PageResult;

import java.util.UUID;

public class ListarTodosPedidos {

    private final PedidoGateway pedidoGateway;

    public ListarTodosPedidos(PedidoGateway pedidoGateway) {
        this.pedidoGateway = pedidoGateway;
    }

    public PageResult<Pedido> execute(boolean admin, UUID usuarioId, int offset, int limit, String sort) {
        return pedidoGateway.findAll(usuarioId, admin, offset, limit, sort);
    }
}
