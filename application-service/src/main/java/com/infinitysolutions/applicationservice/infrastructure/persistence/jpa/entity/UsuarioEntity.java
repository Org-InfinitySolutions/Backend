package com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(
        name = "usuario",
        indexes = {
                @Index(name = "idx_usuario_celular", columnList = "telefone_celular"),
        }
)
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UsuarioEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_usuario", unique = true, nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_endereco", nullable = false)
    private EnderecoEntity enderecoEntity;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "telefone_celular", nullable = false, length = 20)
    private String telefoneCelular;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    @Column(name = "is_ativo", nullable = false)
    private boolean isAtivo;

    @OneToMany(
            mappedBy = "usuario",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ArquivoMetadadosEntity> documentos = new ArrayList<>();

    public UsuarioEntity(String nome, String telefoneCelular) {
        this.nome = nome;
        this.telefoneCelular = telefoneCelular;
        this.isAtivo = true;
    }

    public boolean temDocumentos() {
        return !this.documentos.isEmpty();
    }
}
