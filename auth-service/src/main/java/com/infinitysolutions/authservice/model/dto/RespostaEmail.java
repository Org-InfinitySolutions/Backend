package com.infinitysolutions.authservice.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RespostaEmail(
        String email,
        Boolean disponivel
) {
    public RespostaEmail(String email) {
        this(email, null);
    }
}
