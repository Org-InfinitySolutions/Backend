package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.email;
public record EmailResponseDTO(
    boolean sucesso,
    String mensagem,
    String detalhes
) {
}
