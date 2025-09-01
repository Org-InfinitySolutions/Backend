package com.infinitysolutions.applicationservice.infrastructure.mapper.auth;


import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.CargoEntity;
import com.infinitysolutions.applicationservice.core.domain.valueobject.NomeCargo;

public class CargoMapper {

    private CargoMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static CargoEntity toCargo(String tipoUsuario) {
        switch (tipoUsuario) {
            case "PF" -> {
                String descricao = "Usuário Pessoa Física";
                NomeCargo nomeCargo = NomeCargo.USUARIO_PF;
                return new CargoEntity(nomeCargo, descricao);
            }
            case "PJ" -> {
                String descricao = "Usuário Pessoa Jurídica";
                NomeCargo nomeCargo = NomeCargo.USUARIO_PJ;
                return new CargoEntity(nomeCargo, descricao);
            }
            default -> throw new IllegalArgumentException("Tipo de usuário inválido: " + tipoUsuario);
        }
    }
}
