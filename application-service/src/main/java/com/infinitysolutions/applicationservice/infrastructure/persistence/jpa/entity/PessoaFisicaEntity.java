package com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "pessoa_fisica")
public class PessoaFisicaEntity extends UsuarioEntity {

    @Column(length = 11, nullable = false, unique = true)
    private String cpf;

    @Column(length = 20, nullable = false, unique = true)
    private String rg;

    public PessoaFisicaEntity(String nome, String telefoneCelular, String rg, String cpf) {
        super(nome, telefoneCelular);
        this.rg = rg;
        this.cpf = cpf;
    }
}
