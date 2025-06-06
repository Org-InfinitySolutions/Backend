package com.infinitysolutions.applicationservice.model.produto;

import com.infinitysolutions.applicationservice.model.ArquivoMetadados;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(
            mappedBy = "produto",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<ArquivoMetadados> imagens = new ArrayList<>();

    private String descricao;

    @Column(name = "qtd_estoque")
    private Integer qtdEstoque;

    @Column(name = "is_ativo", nullable = false)
    private boolean isAtivo;

    @ManyToOne
    @JoinColumn(name ="categoria_id")
    private Categoria categoria;

    @PrePersist
    public void prePersist() {
        this.isAtivo = true;
    }
}
