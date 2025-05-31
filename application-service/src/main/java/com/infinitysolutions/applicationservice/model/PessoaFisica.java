package com.infinitysolutions.applicationservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class PessoaFisica {

    @Id
    @Column(name = "fk_usuario")
    private UUID id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @MapsId
    @JoinColumn(name = "fk_usuario")
    @ToString.Exclude
    private Usuario usuario;

    @Column(length = 11, nullable = false, unique = true)
    private String cpf;

    @Column(length = 20, nullable = false, unique = true)
    private String rg;

    @Lob
    @Column(name = "copia_rg")
    private byte[] copiaRg;

    public PessoaFisica(Usuario usuario, String rg, String cpf) {
        this.usuario = usuario;
        this.rg = rg;
        this.cpf = cpf;
    }
}
