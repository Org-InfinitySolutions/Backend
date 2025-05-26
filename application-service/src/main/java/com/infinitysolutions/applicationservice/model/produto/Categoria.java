package com.infinitysolutions.applicationservice.model.produto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "categorias")
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @Column(name = "is_ativo", nullable = false)
    private boolean isAtivo;

    @OneToMany(mappedBy = "categoria")
    private List<Produto> produtos;

    @PrePersist
    public void prePersist(){
        this.isAtivo = true;
    }
}

