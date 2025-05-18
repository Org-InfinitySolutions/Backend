package com.infinitysolutions.applicationservice.model.produto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "produtos")
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private String marca;

    @Column(name = "url_fabricante")
    private String urlFrabricante;

    private byte[] imagem;

    private String descricao;

    @Column(name = "qtd_estoque")
    private Integer qtdEstoque;

    @Column(name = "is_ativo", nullable = false)
    private boolean isAtivo;

    @PrePersist
    public void prePersist() {
        this.isAtivo = true;
    }
}
