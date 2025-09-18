package com.infinitysolutions.applicationservice.infrastructure.gateway.produto;

import com.infinitysolutions.applicationservice.core.domain.mapper.CategoriaMapper;
import com.infinitysolutions.applicationservice.core.domain.produto.Categoria;
import com.infinitysolutions.applicationservice.core.gateway.CategoriaGateway;
import com.infinitysolutions.applicationservice.infrastructure.mapper.produto.ProdutoEntityMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.produto.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoriaGatewayImpl implements CategoriaGateway {

    private final CategoriaRepository categoriaRepository;

    @Override
    public Optional<Categoria> findCategoriaById(Integer id) {
        return categoriaRepository.findByIdAndIsAtivoTrue(id).map((categoria -> CategoriaMapper.toCategoria(
                categoria.getId(),
                categoria.getNome(),
                categoria.isAtivo(),
                categoria.getProdutoEntities().stream().map(ProdutoEntityMapper::toDomain).toList()
        )));
    }
}
