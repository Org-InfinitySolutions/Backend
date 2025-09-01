package com.infinitysolutions.applicationservice.core.domain.valueobject;

import lombok.Getter;

@Getter
public enum NomeCargo {
    FUNCIONARIO(1),
    ADMIN(2),
    USUARIO_PF(3),
    USUARIO_PJ(4);

    private final int id;

    NomeCargo(int id) {
        this.id = id;
    }
    public static NomeCargo fromId(int id) {
        for (NomeCargo cargo : NomeCargo.values()) {
            if (cargo.getId() == id) {
                return cargo;
            }
        }
        throw new IllegalArgumentException("Cargo com ID " + id + " não encontrado.");
    }
}
