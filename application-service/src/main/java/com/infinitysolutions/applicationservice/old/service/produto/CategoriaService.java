package com.infinitysolutions.applicationservice.old.service.produto;

import com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.infrastructure.mapper.produto.CategoriaEntityMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.CategoriaCriacaoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.CategoriaRespostaDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.CategoriaEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.produto.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriaService {
    private final CategoriaRepository repository;

    public List<CategoriaRespostaDTO> listarTodasCategorias() {
        return repository.findAllByIsAtivoTrue()
                .stream()
                .map(CategoriaEntityMapper::toCategoriaRespostaDTO)
                .toList();
    }

    public CategoriaEntity findById(Integer id) {
        log.info("Buscando categoria com ID: {}", id);
        Optional<CategoriaEntity> categoria = repository.findByIdAndIsAtivoTrue(id);
        if (categoria.isEmpty()) {
            log.warn("Categoria com o ID: {} não encontrada", id);
            throw new RecursoNaoEncontradoException("Categoria não encontrada com o id: " + id);
        }
        return categoria.get();
    }

    public CategoriaRespostaDTO buscarPorId(Integer id) {
        return CategoriaEntityMapper.toCategoriaRespostaDTO(findById(id));
    }

    @Transactional
    public CategoriaRespostaDTO criar(CategoriaCriacaoDTO dto) {
        if (repository.existsByNomeIgnoreCase(dto.nome())) {
            throw new RecursoExistenteException("Já existe uma categoria com este nome: " + dto.nome());
        }

        CategoriaEntity categoriaEntity = CategoriaEntityMapper.toCategoria(dto.nome());
        CategoriaEntity categoriaEntitySalva = repository.save(categoriaEntity);

        return CategoriaEntityMapper.toCategoriaRespostaDTO(categoriaEntitySalva);
    }

    @Transactional
    public CategoriaRespostaDTO atualizar(Integer id, CategoriaCriacaoDTO dto) {
        CategoriaEntity categoriaEntity = findById(id);

        if (!categoriaEntity.getNome().equalsIgnoreCase(dto.nome()) &&
                repository.existsByNomeIgnoreCase(dto.nome())) {
            throw new RecursoExistenteException("Já existe outra categoria com este nome: " + dto.nome());
        }

        CategoriaEntityMapper.atualizarCategoria(categoriaEntity, dto);
        CategoriaEntity categoriaEntityAtualizada = repository.save(categoriaEntity);
        return CategoriaEntityMapper.toCategoriaRespostaDTO(categoriaEntityAtualizada);
    }

    @Transactional
    public void remover(Integer id) {
        CategoriaEntity categoriaEntity = findById(id);
        categoriaEntity.setAtivo(false);
        repository.save(categoriaEntity);
    }
}
