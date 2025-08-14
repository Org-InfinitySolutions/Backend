package com.infinitysolutions.applicationservice.model.dto.email;

public record EmailNotificacaoPedidoConcluidoDTO(
        String nomeUsuario, String numeroPedido, String emailUsuario, String qtdItens
) {
}
