package com.infinitysolutions.applicationservice.mapper;


import com.infinitysolutions.applicationservice.model.Estoque;
import com.infinitysolutions.applicationservice.model.dto.estoque.EstoqueCadastroDTO;

public class EstoqueMapper {

    public static Estoque toEstoque(EstoqueCadastroDTO dto) {return new Estoque(dto);}

    public static EstoqueCadastroDTO toDto(Estoque estoque){
        return new EstoqueCadastroDTO(estoque.getNome(), estoque.getCategoria(), estoque.getNumSerie());
    }
}
