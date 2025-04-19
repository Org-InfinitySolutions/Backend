package com.infinitysolutions.applicationservice.model.dto.auth;

import com.infinitysolutions.applicationservice.infra.validation.EmailValido;
import jakarta.validation.constraints.NotNull;

public record UsuarioAutenticacaoValidarCodigoDTO(
        @EmailValido
        String email,
        @NotNull
        String codigo
) {
}
