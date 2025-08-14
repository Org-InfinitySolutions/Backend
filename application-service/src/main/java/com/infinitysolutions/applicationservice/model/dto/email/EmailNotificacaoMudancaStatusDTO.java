package com.infinitysolutions.applicationservice.model.dto.email;

public record EmailNotificacaoMudancaStatusDTO(
        String nomeUsuario, String numeroPedido,
        String statusAnterior, String novoStatus, String emailUsuario
) {
}
