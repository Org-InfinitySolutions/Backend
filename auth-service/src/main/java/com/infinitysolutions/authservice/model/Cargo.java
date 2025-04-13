package com.infinitysolutions.authservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.infinitysolutions.authservice.model.enums.NomeCargo;

@Entity
@Data
@NoArgsConstructor
@Table(name = "cargo")
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private NomeCargo nome;

    @Column(nullable = false)
    private String descricao;

    public Cargo(NomeCargo nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

}
