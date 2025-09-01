package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth;

import com.infinitysolutions.applicationservice.old.infra.validation.EmailValido;
import jakarta.validation.constraints.NotNull;

public record UsuarioAutenticacaoValidarCodigoDTO(
        @EmailValido
        String email,
        @NotNull
        String codigo
) {
}
