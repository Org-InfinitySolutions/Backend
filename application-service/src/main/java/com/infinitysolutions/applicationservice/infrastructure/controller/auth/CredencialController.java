package com.infinitysolutions.applicationservice.infrastructure.controller.auth;

import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth.RequisicaoAlterarEmail;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth.RequisicaoAlterarSenha;
import com.infinitysolutions.applicationservice.old.service.auth.CredencialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Credenciais", description = "Endpoints para gerenciamento de credenciais do usuário")
@RequestMapping("/auth/credenciais")
public class CredencialController {    
    private final CredencialService credencialService;

    @PatchMapping("/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Alterar senha da usuário",
            description = "Permite que o usuário altere sua senha fornecendo a senha atual e a nova senha"
    )
    public void alterarSenha(
            Authentication authentication,
            @Valid @RequestBody RequisicaoAlterarSenha requisicao
    ) {
        UUID usuarioId = UUID.fromString(authentication.getName());
        log.info("Solicitação de alteração de senha para usuário: {}", usuarioId);
        credencialService.alterarSenha(usuarioId, requisicao.senhaAtual(), requisicao.novaSenha());
        log.info("Senha alterada com sucesso para usuário: {}", usuarioId);
    }

    @PatchMapping("/email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Alterar email do usuário",
            description = "Permite que o usuário altere seu email fornecendo a senha atual e o novo email"
    )
    public void alterarEmail(
            Authentication authentication,
            @Valid @RequestBody RequisicaoAlterarEmail requisicao
    ) {
        UUID usuarioId = UUID.fromString(authentication.getName());
        log.info("Solicitação de alteração de email para usuário: {}", usuarioId);
        credencialService.alterarEmail(usuarioId, requisicao.senha(), requisicao.novoEmail());
        log.info("Email alterado com sucesso para usuário: {}", usuarioId);
    }
}