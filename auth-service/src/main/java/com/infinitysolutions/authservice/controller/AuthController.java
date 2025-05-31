package com.infinitysolutions.authservice.controller;


import com.infinitysolutions.authservice.infra.validation.EmailValido;
import com.infinitysolutions.authservice.model.dto.*;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.infinitysolutions.authservice.service.AuthService;
import com.infinitysolutions.authservice.service.CredencialService;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Auth", description = "Endpoints para autenticação e gerenciamento de credenciais")
@RequestMapping("/auth")
public class AuthController {

    private final CredencialService credencialService;
    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Realizar login",
            description = "Autentica um usuário com base no email e senha fornecidos e retorna um token de acesso"
    )
    public RespostaLogin login(@RequestBody RequisicaoLogin requisicaoLogin){
        return authService.realizarLogin(requisicaoLogin.email(), requisicaoLogin.senha());
    }


    @DeleteMapping("/credenciais/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Desativar uma credencial.",
            description = "Desativa a credencial do usuário com base no seu ID."
    )
    public void deletarCredencial(@Valid @RequestBody RequisicaoDeletarCredencial request, @PathVariable UUID usuarioId) {
        credencialService.deletar(usuarioId, request.senha());
    }

    @GetMapping("/credenciais/{usuarioId}/email")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Buscar email da credencial",
            description = "Busca o email de uma credencial de usuário com base no ID do usuário"
    )
    public RespostaEmail buscarEmail(@PathVariable UUID usuarioId) {
        return credencialService.buscarEmail(usuarioId);
    }

    @Operation(
            summary = "Verificar email",
            description = "Verificar se o email solicitado já existe."
    )
    @GetMapping("/email/verificar")
    public ResponseEntity<?> verificarEmail(@RequestParam @EmailValido String email) {
        boolean disponivel = !credencialService.verificarEmailExiste(email);

        if (disponivel) {
            return ResponseEntity.ok(new RespostaEmail(email, true));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new RespostaEmail(email, false));
        }
    }

    @PostMapping("/cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    @Hidden
    @Operation(
            summary = "Cadastrar nova credencial",
            description = "Cadastra uma nova credencial de um usuário com base no seu id e informações de login"
    )
    public void cadastrar(@Valid @RequestBody RequisicaoCadastro requisicaoCadastro) {
        credencialService.criarCredencialUsuario(requisicaoCadastro.idUsuario(), requisicaoCadastro.email(), requisicaoCadastro.senha(), requisicaoCadastro.tipoUsuario());
    }
}
