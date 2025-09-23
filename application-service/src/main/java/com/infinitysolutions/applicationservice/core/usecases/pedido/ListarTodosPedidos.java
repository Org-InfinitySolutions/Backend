package com.infinitysolutions.applicationservice.core.usecases.pedido;


import com.infinitysolutions.applicationservice.core.domain.pedido.Pedido;
import com.infinitysolutions.applicationservice.core.gateway.PedidoGateway;

import java.util.List;
import java.util.UUID;

public class ListarTodosPedidos {

    private final PedidoGateway pedidoGateway;

    public ListarTodosPedidos(PedidoGateway pedidoGateway) {
        this.pedidoGateway = pedidoGateway;
    }

    public List<Pedido> execute(boolean admin, UUID uuid) {
        return pedidoGateway.findAll(uuid, admin);
    }
}
