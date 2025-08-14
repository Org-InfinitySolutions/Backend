package com.infinitysolutions.applicationservice.model.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RespostaEmailDTO(
        String email,
        Boolean disponivel
) {
    public RespostaEmailDTO(String email) {
        this(email, null);
    }
}
