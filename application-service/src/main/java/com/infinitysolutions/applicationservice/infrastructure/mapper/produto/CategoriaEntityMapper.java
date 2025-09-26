package com.infinitysolutions.applicationservice.infrastructure.mapper.produto;

import com.infinitysolutions.applicationservice.core.domain.produto.Categoria;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.CategoriaCriacaoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.CategoriaRespostaDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.CategoriaEntity;

public class CategoriaEntityMapper {
    public static CategoriaRespostaDTO toCategoriaRespostaDTO(CategoriaEntity categoriaEntity) {
        return new CategoriaRespostaDTO(
                categoriaEntity.getId(),
                categoriaEntity.getNome()
        );
    }

    public static CategoriaRespostaDTO toCategoriaRespostaDTO(Categoria categoria) {
        return new CategoriaRespostaDTO(
                categoria.getId(),
                categoria.getNome()
        );
    }

    public static CategoriaEntity toCategoria(String nomeCategoria) {
        CategoriaEntity categoriaEntity = new CategoriaEntity();
        categoriaEntity.setNome(nomeCategoria);
        return categoriaEntity;
    }

    public static void atualizarCategoria(CategoriaEntity categoriaEntity, CategoriaCriacaoDTO dto) {
        categoriaEntity.setNome(dto.nome());
    }
}
