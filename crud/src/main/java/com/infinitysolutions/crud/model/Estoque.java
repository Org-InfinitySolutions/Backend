package com.infinitysolutions.crud.model;

import com.infinitysolutions.crud.model.dto.EstoqueDtoCriacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Data
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "")
    private String nome;

    @NotBlank(message = "")
    private String numSerie;

    @NotBlank(message = "")
    private String categoria;

    public Estoque(EstoqueDtoCriacao dto){
        this.nome = dto.nome();
        this.numSerie = dto.numSerie();
        this.categoria = dto.categoria();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
