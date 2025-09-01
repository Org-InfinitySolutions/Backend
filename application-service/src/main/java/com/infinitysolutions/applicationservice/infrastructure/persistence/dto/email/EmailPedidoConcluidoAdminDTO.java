package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.email;

public record EmailPedidoConcluidoAdminDTO(
        String nomeUsuario, String numeroPedido, String emailUsuario, String qtdItens, String telefoneUsuario, String descricaoPedido
) {
}
