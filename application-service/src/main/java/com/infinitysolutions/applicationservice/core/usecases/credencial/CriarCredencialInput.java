package com.infinitysolutions.applicationservice.core.usecases.credencial;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import com.infinitysolutions.applicationservice.core.domain.valueobject.Senha;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoUsuario;

import java.util.UUID;

public record CriarCredencialInput(
    Email email,
    Senha senha,
    UUID idUsuario,
    TipoUsuario tipoUsuario
) {
    public static CriarCredencialInput of(String emailString, String senhaString, UUID idUsuario, TipoUsuario tipoUsuario) {
        Email email = Email.of(emailString);
        Senha senha = Senha.ofSistema(senhaString);
        return new CriarCredencialInput(email, senha, idUsuario, tipoUsuario);
    }
}
