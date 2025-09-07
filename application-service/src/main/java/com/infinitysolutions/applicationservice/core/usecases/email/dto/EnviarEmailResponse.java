package com.infinitysolutions.applicationservice.core.usecases.email.dto;
public record EnviarEmailResponse(
    boolean sucesso,
    String mensagem,
    String detalhes
) {
}
