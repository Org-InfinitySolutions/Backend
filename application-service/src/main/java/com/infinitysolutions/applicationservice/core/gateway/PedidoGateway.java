package com.infinitysolutions.applicationservice.core.gateway;

import com.infinitysolutions.applicationservice.core.domain.pedido.Pedido;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface PedidoGateway {
    Pedido save(Pedido pedidoCriado);
    Pedido atualizarStatus(Pedido pedido);
    Optional<Pedido> findById(Integer pedidoId, UUID idUsuario, boolean isAdmin);
    List<Pedido> findAll(UUID uuid, boolean admin);
}
