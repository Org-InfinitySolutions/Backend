package com.infinitysolutions.applicationservice.model.dto.email;

public record EmailNotificacaoPedidoConcluidoAdminDTO(
        String nomeUsuario, String numeroPedido, String emailUsuario, String qtdItens, String telefoneUsuario, String descricaoPedido
) {
}
