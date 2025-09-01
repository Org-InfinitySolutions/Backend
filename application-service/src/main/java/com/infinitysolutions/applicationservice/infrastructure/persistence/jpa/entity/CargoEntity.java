package com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.infinitysolutions.applicationservice.core.domain.valueobject.NomeCargo;

@Entity
@Data
@NoArgsConstructor
@Table(name = "cargo")
public class CargoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private NomeCargo nome;

    @Column(nullable = false)
    private String descricao;

    public CargoEntity(NomeCargo nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

}
