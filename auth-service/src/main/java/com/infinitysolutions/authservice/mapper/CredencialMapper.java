package com.infinitysolutions.authservice.mapper;

import com.infinitysolutions.authservice.model.Credencial;

import java.util.UUID;



public class CredencialMapper {

    public static Credencial toCredencial(UUID idUsuario, String email, String senha) {
        return new Credencial(idUsuario, email, senha);
    }

}
