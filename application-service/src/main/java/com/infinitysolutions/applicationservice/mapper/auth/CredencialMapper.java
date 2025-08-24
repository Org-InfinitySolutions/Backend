package com.infinitysolutions.applicationservice.mapper.auth;


import com.infinitysolutions.applicationservice.model.auth.Credencial;

import java.util.UUID;



public class CredencialMapper {
    private CredencialMapper(){
     throw new IllegalStateException("Utility class");
    }
    public static Credencial toCredencial(UUID idUsuario, String email, String senha) {
        return new Credencial(idUsuario, email, senha);
    }

}
