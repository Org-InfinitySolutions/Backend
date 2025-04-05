package com.infinitysolutions.applicationservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class PessoaJuridica {

    @Id
    @Column(name = "fk_usuario")
    private UUID id;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "fk_usuario")
    private Usuario usuario;

    @Column(length = 14, nullable = false, unique = true)
    private String cnpj;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    @Lob
    @Column(name = "contrato_social")
    private byte[] contratoSocial;

    @Lob
    @Column(name = "cartao_cnpj")
    private byte[] cartaoCnpj;
}
