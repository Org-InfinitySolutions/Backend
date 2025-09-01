package com.infinitysolutions.applicationservice.infrastructure.mapper.produto;

import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.CategoriaCriacaoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.CategoriaRespostaDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.Categoria;

public class CategoriaMapper {
    public static CategoriaRespostaDTO toCategoriaRespostaDTO(Categoria categoria) {
        return new CategoriaRespostaDTO(
                categoria.getId(),
                categoria.getNome()
        );
    }

    public static Categoria toCategoria(CategoriaCriacaoDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome());
        return categoria;
    }

    public static void atualizarCategoria(Categoria categoria, CategoriaCriacaoDTO dto) {
        categoria.setNome(dto.nome());
    }
}
