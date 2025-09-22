package com.infinitysolutions.applicationservice.core.gateway;

import com.infinitysolutions.applicationservice.core.domain.pedido.Pedido;


public interface PedidoGateway {
    Pedido save(Pedido pedidoCriado);
}
