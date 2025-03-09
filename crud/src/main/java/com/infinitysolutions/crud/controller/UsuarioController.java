package com.infinitysolutions.crud.controller;

import com.infinitysolutions.crud.mapper.UsuarioMapper;
import com.infinitysolutions.crud.model.Usuario;
import com.infinitysolutions.crud.model.dto.UsuarioDtoCriacao;
import com.infinitysolutions.crud.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody UsuarioDtoCriacao dto) {
        Usuario usuario = UsuarioMapper.toUsuario(dto);
        Usuario usuarioSalvo = usuarioService.criar(usuario);
        return new ResponseEntity<>(usuarioSalvo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Integer id, @RequestBody UsuarioDtoCriacao usuarioDto) {
        Usuario usuarioAtualizado = usuarioService.atualizar(id, UsuarioMapper.toUsuario(usuarioDto));
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Integer id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
