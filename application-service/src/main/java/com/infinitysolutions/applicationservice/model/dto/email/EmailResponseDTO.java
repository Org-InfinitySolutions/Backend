package com.infinitysolutions.applicationservice.model.dto.email;
public record EmailResponseDTO(
    boolean sucesso,
    String mensagem,
    String detalhes
) {
}
