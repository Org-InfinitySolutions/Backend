package com.infinitysolutions.applicationservice.controller;

import com.infinitysolutions.applicationservice.model.dto.UsuarioCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.UsuarioRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@AllArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UsuarioController {
    
    private final UsuarioService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Cadastrar um novo usuário",
        description = "Cadastra um novo usuário no sistema, podendo ser pessoa física (PF) ou pessoa jurídica (PJ)"
    )

    public UsuarioRespostaCadastroDTO cadastrar(@Valid @RequestBody UsuarioCadastroDTO usuarioCadastroDTO) {
        return service.cadastrar(usuarioCadastroDTO);
    }
}
