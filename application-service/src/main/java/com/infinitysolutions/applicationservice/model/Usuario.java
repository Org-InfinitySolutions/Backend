package com.infinitysolutions.applicationservice.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
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
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_usuario", unique = true, nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_endereco", nullable = false)
    private Endereco endereco;

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

    @Lob
    @Column(name = "comprovante_endereco")
    private byte[] comprovanteEndereco;
    
    @Column(name = "comprovante_nome_arquivo")
    private String comprovanteNomeArquivo;
    
    @Column(name = "comprovante_tipo_arquivo") 
    private String comprovanteTipoArquivo;
    
    @Column(name = "comprovante_data_upload")
    private LocalDateTime comprovanteDataUpload;

    public Usuario(String nome, String telefoneCelular) {
        this.nome = nome;
        this.telefoneCelular = telefoneCelular;
        this.isAtivo = true;
    }
}
