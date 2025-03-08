package com.infinitysolutions.crud.service;

import com.infinitysolutions.crud.model.Estoque;
import com.infinitysolutions.crud.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class EstoqueService {
    @Autowired
    private EstoqueRepository repository;

    public Estoque criar(Estoque estoque) {
        return repository.save(estoque);
    }

    public List<Estoque> listar() {
        return repository.findAll();
    }

    public Estoque buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estoque não encontrado para o ID: " + id));
    }

    public Estoque atualizar(Integer id, Estoque estoqueAtualizado) {
        Estoque estoqueExistente = buscarPorId(id);
        if (!estoqueExistente.getId().equals(id)) {
            throw new IllegalArgumentException("A atualização do ID não é permitida.");
        }
        estoqueExistente.setNome(estoqueAtualizado.getNome());
        estoqueExistente.setNumSerie(estoqueAtualizado.getNumSerie());
        estoqueExistente.setCategoria(estoqueAtualizado.getCategoria());
        return repository.save(estoqueExistente);
    }

    public void deletar(Integer id) {
        Estoque estoque = buscarPorId(id);
        repository.delete(estoque);
    }
}




