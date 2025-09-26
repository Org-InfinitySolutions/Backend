package com.infinitysolutions.applicationservice.infrastructure.mapper.auth;


import com.infinitysolutions.applicationservice.core.domain.usuario.Cargo;
import com.infinitysolutions.applicationservice.core.domain.usuario.Credencial;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.CargoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.CredencialEntity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


public class CredencialMapper {
    private CredencialMapper(){
     throw new IllegalStateException("Utility class");
    }

    public static CredencialEntity toEntity(UUID idUsuario, String email, String senha, Set<Cargo> cargos, LocalDateTime ultimoLogin, boolean isAtivo) {
        CredencialEntity entity = new CredencialEntity(idUsuario, email, senha, isAtivo);
        entity.setUltimoLogin(ultimoLogin);
        Set<CargoEntity> cargoEntities = cargos.stream().map(CargoMapper::toEntity).collect(Collectors.toSet());
        entity.setCargoEntities(cargoEntities);
        return entity;
    }

    public static Credencial toCredencial(CredencialEntity entity) {
        Set<Cargo> cargos = entity.getCargoEntities().stream().map(CargoMapper::toDomain).collect(Collectors.toSet());
        return Credencial.ofEntity(entity.getFkUsuario(), entity.getEmail(), entity.getHashSenha(), cargos, entity.getUltimoLogin());
    }

}
