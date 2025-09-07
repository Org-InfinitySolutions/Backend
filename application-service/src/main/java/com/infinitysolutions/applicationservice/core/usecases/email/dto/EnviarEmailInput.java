package com.infinitysolutions.applicationservice.core.usecases.email.dto;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;

public record EnviarEmailInput(
    String nome,
    Email email
) {

}
