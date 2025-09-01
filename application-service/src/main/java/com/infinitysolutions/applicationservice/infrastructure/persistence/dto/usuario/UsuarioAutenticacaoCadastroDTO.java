package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario;

import com.infinitysolutions.applicationservice.old.infra.validation.EmailValido;
import jakarta.validation.constraints.NotBlank;

public record UsuarioAutenticacaoCadastroDTO(
    @NotBlank(message = "Nome não pode ser nulo ou vazio")
    String nome,
    @EmailValido
    String email
) {

}
