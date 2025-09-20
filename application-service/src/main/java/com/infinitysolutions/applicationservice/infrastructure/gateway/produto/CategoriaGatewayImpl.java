package com.infinitysolutions.applicationservice.infrastructure.gateway.produto;

import com.infinitysolutions.applicationservice.core.domain.mapper.CategoriaMapper;
import com.infinitysolutions.applicationservice.core.domain.produto.Categoria;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.gateway.CategoriaGateway;
import com.infinitysolutions.applicationservice.infrastructure.mapper.produto.CategoriaEntityMapper;
import com.infinitysolutions.applicationservice.infrastructure.mapper.produto.ProdutoEntityMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.CategoriaEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.produto.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
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

    @Override
    public List<Categoria> findAll() {
        return categoriaRepository.findAllByIsAtivoTrue().stream().map(entity -> CategoriaMapper.toCategoria(
                entity.getId(),
                entity.getNome(),
                entity.isAtivo(),
                null
        )).toList();
    }

    @Override
    public boolean existsByNome(String nomeCategoria) {
        return categoriaRepository.existsByNomeIgnoreCase(nomeCategoria);
    }

    @Override
    public Categoria save(String nomeCategoria) {
        CategoriaEntity saved = categoriaRepository.save(CategoriaEntityMapper.toCategoria(nomeCategoria));
        return CategoriaMapper.toCategoria(saved.getId(), saved.getNome(), saved.isAtivo(), null);
    }

    @Override
    public Categoria update(Categoria categoria) {
        log.info("Atualizando categoria " + categoria.getNome());
        CategoriaEntity categoriaEntity = categoriaRepository.findByIdAndIsAtivoTrue(categoria.getId()).orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada com o id " + categoria.getId()));
        categoriaEntity.setNome(categoria.getNome());
        categoriaEntity.setAtivo(categoria.isAtivo());
        CategoriaEntity saved = categoriaRepository.save(categoriaEntity);
        return CategoriaMapper.toCategoria(saved.getId(), saved.getNome(), saved.isAtivo(), null);
    }
}
