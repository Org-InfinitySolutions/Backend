package com.infinitysolutions.applicationservice.core.usecases.email.dto;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;

public record EmailNotificacaoMudancaStatus(
        String nomeUsuario, String numeroPedido,
        String statusAnterior, String novoStatus, Email emailUsuario
) {
}
