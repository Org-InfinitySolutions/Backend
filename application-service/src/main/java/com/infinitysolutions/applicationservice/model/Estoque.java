package com.infinitysolutions.applicationservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.infinitysolutions.applicationservice.model.dto.estoque.EstoqueCadastroDTO;


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

    public Estoque(EstoqueCadastroDTO dto){
        this.nome = dto.nome();
        this.numSerie = dto.numSerie();
        this.categoria = dto.categoria();
    }
}
