package com.infinitysolutions.crud.service;

import com.infinitysolutions.crud.model.Usuario;
import com.infinitysolutions.crud.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    public Usuario criar(Usuario usuario) {
        return repository.save(usuario);
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Usuario buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado para o ID: " + id));
    }

    public Usuario atualizar(Integer id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = buscarPorId(id);
        if (!usuarioExistente.getId().equals(id)) {
            throw new IllegalArgumentException("A atualização do ID não é permitida.");
        }
        usuarioExistente.setNomeCompleto(usuarioAtualizado.getNomeCompleto());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        return repository.save(usuarioExistente);
    }

    public void deletar(Integer id) {
        Usuario usuario = buscarPorId(id);
        repository.delete(usuario);
    }
}