package com.infinitysolutions.applicationservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(
        name = "pessoa_juridica",
        indexes = {
                @Index(name = "idx_pj_telefone_residencial", columnList = "telefone_residencial"),
        }
)
public class PessoaJuridica {

    @Id
    @Column(name = "fk_usuario")
    private UUID id;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "fk_usuario")
    @ToString.Exclude
    private Usuario usuario;

    @Column(name = "telefone_residencial", nullable = false, length = 20)
    private String telefoneResidencial;

    @Column(length = 14, nullable = false, unique = true)
    private String cnpj;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    public PessoaJuridica(Usuario usuario, String telefoneResidencial, String cnpj, String razaoSocial) {
        this.usuario = usuario;
        this.telefoneResidencial = telefoneResidencial;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
    }
}
