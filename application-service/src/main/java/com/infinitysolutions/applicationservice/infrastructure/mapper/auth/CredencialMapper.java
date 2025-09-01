package com.infinitysolutions.applicationservice.infrastructure.mapper.auth;


import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.CredencialEntity;

import java.util.UUID;



public class CredencialMapper {
    private CredencialMapper(){
     throw new IllegalStateException("Utility class");
    }
    public static CredencialEntity toCredencial(UUID idUsuario, String email, String senha) {
        return new CredencialEntity(idUsuario, email, senha);
    }

}
