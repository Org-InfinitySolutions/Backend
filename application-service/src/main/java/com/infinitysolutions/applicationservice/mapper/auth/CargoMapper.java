package com.infinitysolutions.applicationservice.mapper.auth;


import com.infinitysolutions.applicationservice.model.auth.Cargo;
import com.infinitysolutions.applicationservice.model.auth.enums.NomeCargo;

public class CargoMapper {

    private CargoMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Cargo toCargo(String tipoUsuario) {
        switch (tipoUsuario) {
            case "PF" -> {
                String descricao = "Usuário Pessoa Física";
                NomeCargo nomeCargo = NomeCargo.USUARIO_PF;
                return new Cargo(nomeCargo, descricao);
            }
            case "PJ" -> {
                String descricao = "Usuário Pessoa Jurídica";
                NomeCargo nomeCargo = NomeCargo.USUARIO_PJ;
                return new Cargo(nomeCargo, descricao);
            }
            default -> throw new IllegalArgumentException("Tipo de usuário inválido: " + tipoUsuario);
        }
    }
}
