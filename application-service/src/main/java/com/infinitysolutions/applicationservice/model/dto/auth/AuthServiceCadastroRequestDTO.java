package com.infinitysolutions.applicationservice.model.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record AuthServiceCadastroRequestDTO (
        String email,
        String senha,
        @JsonProperty("id_usuario")
        UUID idUsuario,
        @JsonProperty("tipo_usuario")
        String tipoUsuario
) {
}
