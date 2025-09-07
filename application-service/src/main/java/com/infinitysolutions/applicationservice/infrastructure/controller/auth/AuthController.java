package com.infinitysolutions.applicationservice.infrastructure.controller.auth;


import com.infinitysolutions.applicationservice.core.gateway.CredenciaisGateway;
import com.infinitysolutions.applicationservice.core.usecases.credencial.BuscarCredencialPorId;
import com.infinitysolutions.applicationservice.core.usecases.credencial.DeletarCredencial;
import com.infinitysolutions.applicationservice.core.usecases.credencial.RealizarLogin;
import com.infinitysolutions.applicationservice.core.usecases.credencial.RespostaLogin;
import com.infinitysolutions.applicationservice.old.infra.validation.EmailValido;

import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth.*;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Auth", description = "Endpoints para autenticação e gerenciamento de credenciais")
@RequestMapping("/auth")
public class AuthController {

    private final RealizarLogin realizarLogin;
    private final DeletarCredencial deletarCredencial;
    private final BuscarCredencialPorId buscarCredencialPorId;
    private final CredenciaisGateway credenciaisGateway;


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Realizar login",
            description = "Autentica um usuário com base no email e senha fornecidos e retorna um token de acesso"
    )
    public RespostaLogin login(@RequestBody RequisicaoLogin requisicaoLogin){
        return realizarLogin.execute(requisicaoLogin.email(), requisicaoLogin.senha());
    }
    @DeleteMapping("/credenciais/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Desativar uma credencial.",
            description = "Desativa a credencial do usuário com base no seu ID."
    )
    @Transactional
    public void deletarCredencial(@Valid @RequestBody RequisicaoDeletarCredencial request, @PathVariable UUID usuarioId) {
        deletarCredencial.execute(usuarioId, request.senha());
    }

    @GetMapping("/credenciais/{usuarioId}/email")
    @ResponseStatus(HttpStatus.OK)
    @Hidden
    @Operation(
            summary = "Buscar email da credencial",
            description = "Busca o email de uma credencial de usuário com base no ID do usuário"
    )
    public RespostaEmail buscarEmail(@PathVariable UUID usuarioId) {
        return new RespostaEmail(buscarCredencialPorId.execute(usuarioId).getEmail().getValor());
    }

    @Operation(
            summary = "Verificar email",
            description = "Verificar se o email solicitado já existe."
    )
    @GetMapping("/email/verificar")
    public ResponseEntity<?> verificarEmail(@RequestParam @EmailValido String email) {
        boolean disponivel = !credenciaisGateway.existsByEmail(email);

        if (disponivel) {
            return ResponseEntity.ok(new RespostaEmail(email, true));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new RespostaEmail(email, false));
        }
    }
}
