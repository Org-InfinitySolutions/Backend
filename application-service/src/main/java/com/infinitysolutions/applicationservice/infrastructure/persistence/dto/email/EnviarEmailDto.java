package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.email;

import com.infinitysolutions.applicationservice.infrastructure.utils.validation.EmailValido;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EnviarEmailDto {
    @NotBlank
    private String nome;
    @EmailValido
    private String email;
}
