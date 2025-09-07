package com.infinitysolutions.applicationservice.core.usecases.email.dto;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;

public record EmailPedidoConcluidoAdmin(
        String nomeUsuario, String numeroPedido, Email emailUsuario, String qtdItens, String telefoneUsuario, String descricaoPedido
) {
}
