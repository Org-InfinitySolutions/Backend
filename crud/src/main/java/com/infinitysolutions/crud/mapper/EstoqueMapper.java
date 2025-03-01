package com.infinitysolutions.crud.mapper;


import com.infinitysolutions.crud.model.Estoque;
import com.infinitysolutions.crud.model.dto.EstoqueDtoCriacao;

public class EstoqueMapper {

    public static Estoque toEstoque(EstoqueDtoCriacao dto) {return new Estoque(dto);}

    public static EstoqueDtoCriacao toDto(Estoque estoque){
        return new EstoqueDtoCriacao(estoque.getNome(), estoque.getCategoria(), estoque.getNumSerie());
    }
}
