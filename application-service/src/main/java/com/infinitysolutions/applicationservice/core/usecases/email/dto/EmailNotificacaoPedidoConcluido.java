package com.infinitysolutions.applicationservice.core.usecases.email.dto;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;

public record EmailNotificacaoPedidoConcluido(
        String nomeUsuario, String numeroPedido, Email emailUsuario, String qtdItens
) {
}
