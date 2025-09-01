package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.email;

public record EmailNotificacaoPedidoConcluidoDTO(
        String nomeUsuario, String numeroPedido, String emailUsuario, String qtdItens
) {
}
