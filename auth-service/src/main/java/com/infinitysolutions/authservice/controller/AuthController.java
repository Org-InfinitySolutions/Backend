package com.infinitysolutions.authservice.controller;


import com.infinitysolutions.authservice.model.dto.RespostaLogin;
import com.infinitysolutions.authservice.model.dto.RequisicaoEstado;
import com.infinitysolutions.authservice.model.dto.RequisicaoLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.infinitysolutions.authservice.model.dto.RequisicaoCadastro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.infinitysolutions.authservice.service.AuthService;
import com.infinitysolutions.authservice.service.CredencialService;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Autenticação", description = "Endpoints para autenticação e gerenciamento de credenciais")
public class AuthController {

    private final CredencialService credencialService;
    private final AuthService authService;



    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public RespostaLogin login(@RequestBody RequisicaoLogin requisicaoLogin){
        return authService.realizarLogin(requisicaoLogin.email(), requisicaoLogin.senha());
    }


    @PostMapping("/credenciais/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Atualizar estado da credencial",
            description = "Atualiza o estado (true/false) de uma credencial de usuário com base no ID do usuário"
    )
    public void atualizarEstado(@Valid @RequestBody RequisicaoEstado requisicaoEstado, @PathVariable UUID usuarioId) {
        credencialService.atualizarEstado(usuarioId, requisicaoEstado.ativo());
    }

    @PostMapping("/cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Cadastrar nova credencial",
            description = "Cadastra uma nova credencial de um usuário com base no seu id e informações de login"
    )
    public void cadastrar(@Valid @RequestBody RequisicaoCadastro requisicaoCadastro) {
        credencialService.criarCredencialUsuario(requisicaoCadastro.idUsuario(), requisicaoCadastro.email(), requisicaoCadastro.senha(), requisicaoCadastro.tipoUsuario());
    }
}
