package com.infinitysolutions.applicationservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor

@Table(name = "endereco",
        indexes = {
                @Index(name = "uq_idx_endereco_completo",
                        columnList = "cep, logradouro(100), numero, bairro(100), cidade(100), estado, complemento(100)",
                        unique = true)
        })
public class Endereco {

    @Id
    @Column(name = "id_endereco", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cep", nullable = false, length = 10)
    private String cep;

    @Column(name = "logradouro", nullable = false, length = 255)
    private String logradouro;

    @Column(name = "bairro", nullable = false, length = 255)
    private String bairro;

    @Column(name = "cidade", nullable = false, length = 255)
    private String cidade;

    @Column(name = "estado", nullable = false, length = 2)
    private String estado;

    @Column(name = "numero", nullable = false, length = 20)
    private String numero;

    @Column(name = "complemento", length = 255, nullable = true)
    private String complemento;

    public Endereco(String cep, String logradouro, String bairro, String cidade, String estado, String numero, String complemento) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.numero = numero;
        this.complemento = complemento;
    }
}
