package com.infinitysolutions.applicationservice.infrastructure.controller.auth;

import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth.RequisicaoResetSenhaEmail;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth.RequisicaoResetSenhaCodigo;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth.RespostaResetSenha;
import com.infinitysolutions.applicationservice.old.service.auth.ResetSenhaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reset de Senha", description = "Endpoints para reset de senha via email")
@RequestMapping("/auth/reset-senha")
public class ResetSenhaController {

    private final ResetSenhaService resetSenhaService;

    @PostMapping("/solicitar")
    @Operation(
            summary = "Solicitar código de reset de senha",
            description = "Envia um código de verificação por email para reset de senha"
    )
    public ResponseEntity<RespostaResetSenha> solicitarResetSenha(@Valid @RequestBody RequisicaoResetSenhaEmail requisicao) {
        log.info("Solicitação de reset de senha recebida para email: {}", requisicao.email());
        
        RespostaResetSenha resposta = resetSenhaService.enviarCodigoResetSenha(requisicao.email());
        
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/confirmar")
    @Operation(
            summary = "Confirmar reset de senha com código",
            description = "Valida o código recebido por email e define a nova senha"
    )
    public ResponseEntity<RespostaResetSenha> confirmarResetSenha(@Valid @RequestBody RequisicaoResetSenhaCodigo requisicao) {
        log.info("Confirmação de reset de senha recebida para email: {}", requisicao.email());
        
        RespostaResetSenha resposta = resetSenhaService.resetarSenhaComCodigo(
                requisicao.email(), 
                requisicao.codigo(), 
                requisicao.novaSenha()
        );
        
        if (!resposta.sucesso()) {
            String mensagem = resposta.mensagem();
            
            if (mensagem.contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
            } else if (mensagem.contains("expirado") || mensagem.contains("inválido")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
            } else if (mensagem.contains("excedido")) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(resposta);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resposta);
            }
        }
        
        return ResponseEntity.ok(resposta);
    }
}