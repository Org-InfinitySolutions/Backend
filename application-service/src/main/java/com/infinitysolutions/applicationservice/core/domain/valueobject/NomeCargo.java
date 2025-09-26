package com.infinitysolutions.applicationservice.core.domain.valueobject;

import lombok.Getter;

@Getter
public enum NomeCargo {
    FUNCIONARIO(1, "Usuário Funcionário"),
    ADMIN(2, "Usuário Admin"),
    USUARIO_PF(3, "Usuário Pessoa Física"),
    USUARIO_PJ(4, "Usuário Pessoa Jurídica");

    private final int id;
    private final String descricao;

    NomeCargo(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
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
