package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.email;

public record EmailNotificacaoMudancaStatusDTO(
        String nomeUsuario, String numeroPedido,
        String statusAnterior, String novoStatus, String emailUsuario
) {
}
