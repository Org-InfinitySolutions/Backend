package com.infinitysolutions.applicationservice.service.produto;

import com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.infrastructure.mapper.produto.CategoriaMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.CategoriaCriacaoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.CategoriaRespostaDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.Categoria;
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
                .map(CategoriaMapper::toCategoriaRespostaDTO)
                .toList();
    }

    public Categoria findById(Integer id) {
        log.info("Buscando categoria com ID: {}", id);
        Optional<Categoria> categoria = repository.findByIdAndIsAtivoTrue(id);
        if (categoria.isEmpty()) {
            log.warn("Categoria com o ID: {} não encontrada", id);
            throw new RecursoNaoEncontradoException("Categoria não encontrada com o id: " + id);
        }
        return categoria.get();
    }

    public CategoriaRespostaDTO buscarPorId(Integer id) {
        return CategoriaMapper.toCategoriaRespostaDTO(findById(id));
    }

    @Transactional
    public CategoriaRespostaDTO criar(CategoriaCriacaoDTO dto) {
        if (repository.existsByNomeIgnoreCase(dto.nome())) {
            throw new RecursoExistenteException("Já existe uma categoria com este nome: " + dto.nome());
        }

        Categoria categoria = CategoriaMapper.toCategoria(dto);
        Categoria categoriaSalva = repository.save(categoria);

        return CategoriaMapper.toCategoriaRespostaDTO(categoriaSalva);
    }

    @Transactional
    public CategoriaRespostaDTO atualizar(Integer id, CategoriaCriacaoDTO dto) {
        Categoria categoria = findById(id);

        if (!categoria.getNome().equalsIgnoreCase(dto.nome()) &&
                repository.existsByNomeIgnoreCase(dto.nome())) {
            throw new RecursoExistenteException("Já existe outra categoria com este nome: " + dto.nome());
        }

        CategoriaMapper.atualizarCategoria(categoria, dto);
        Categoria categoriaAtualizada = repository.save(categoria);
        return CategoriaMapper.toCategoriaRespostaDTO(categoriaAtualizada);
    }

    @Transactional
    public void remover(Integer id) {
        Categoria categoria = findById(id);
        categoria.setAtivo(false);
        repository.save(categoria);
    }
}
