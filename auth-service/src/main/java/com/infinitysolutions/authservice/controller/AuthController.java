package com.infinitysolutions.authservice.controller;


import com.infinitysolutions.authservice.infra.exception.AutenticacaoException;
import com.infinitysolutions.authservice.model.dto.RequisicaoEstado;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.infinitysolutions.authservice.model.dto.RequisicaoCadastro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.infinitysolutions.authservice.service.AuthService;
import com.infinitysolutions.authservice.service.CredencialService;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
@Tag(name = "Autenticação", description = "Endpoints para autenticação e gerenciamento de credenciais")
public class AuthController {

    private final CredencialService credencialService;
    private final AuthService authService;



//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody RequisicaoLogin requisicaoLogin){
//        try {
//            return ResponseEntity.ok(authService.autenticar);
//        } catch (AuthenticationException e) {
//            return new ResponseEntity<>(new ErrorResponse("credenciais_invalidas", "Credenciais invalidas"), HttpStatus.UNAUTHORIZED);
//        }
//    }


    @PostMapping("/credenciais/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarEstado(@Valid @RequestBody RequisicaoEstado requisicaoEstado, @PathVariable UUID usuarioId) {
        credencialService.atualizarEstado(usuarioId, requisicaoEstado.ativo());
    }

    @PostMapping("/cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    public void cadastrar(@Valid @RequestBody RequisicaoCadastro requisicaoCadastro) {
        credencialService.criarCredencialUsuario(requisicaoCadastro.idUsuario(), requisicaoCadastro.email(), requisicaoCadastro.senha(), requisicaoCadastro.tipoUsuario());
    }
}
