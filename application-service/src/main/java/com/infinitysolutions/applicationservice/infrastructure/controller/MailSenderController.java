package com.infinitysolutions.applicationservice.infrastructure.controller;

import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth.UsuarioAutenticacaoValidarCodigoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioAutenticacaoCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.email.EmailResponseDTO;
import com.infinitysolutions.applicationservice.old.service.email.CodigoAutenticacaoService;
import com.infinitysolutions.applicationservice.old.service.email.EnvioEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
@Validated
@Tag(name = "Emails", description = "Endpoints para verificação de email e envio de códigos de autenticação")
public class MailSenderController {

    private final EnvioEmailService emailAutenticacaoService;
    private final CodigoAutenticacaoService codigoAutenticacaoService;

    @PostMapping("/enviar-codigo")
    @Operation(
        summary = "Enviar código de verificação",
        description = "Envia um código de verificação para o email informado durante o processo de cadastro"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Código enviado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "500", description = "Erro interno ao enviar email")
    })
    @ResponseStatus(HttpStatus.OK)
    public EmailResponseDTO enviarCodigoVerificacao(@RequestBody @Valid UsuarioAutenticacaoCadastroDTO dto) {
            return emailAutenticacaoService.enviarCodigoAutenticacao(dto);
    }
    @PostMapping("/validar-codigo")
    @Operation(
        summary = "Validar código de verificação",
        description = "Valida o código enviado para o email do usuário durante o processo de cadastro"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Código validado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "404", description = "Código não encontrado para o email"),
        @ApiResponse(responseCode = "410", description = "Código expirado"),
        @ApiResponse(responseCode = "429", description = "Número máximo de tentativas excedido")
    })
    public ResponseEntity<EmailResponseDTO> validarCodigo(@RequestBody @Valid UsuarioAutenticacaoValidarCodigoDTO dto) {
            var response = codigoAutenticacaoService.validarCodigoAutenticacao(dto.email(), dto.codigo());
            if (response.getKey()) {
                return ResponseEntity.ok(
                    new EmailResponseDTO(true, "Código validado com sucesso", response.getValue())
                );
            } else {
                String mensagem = response.getValue();
                if (mensagem.contains("sem código ativo")) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new EmailResponseDTO(false, "Código não encontrado para o email informado", mensagem));
                } else if (mensagem.contains("Código expirado")) {
                    return ResponseEntity.status(HttpStatus.GONE)
                        .body(new EmailResponseDTO(false, "Código expirado", mensagem));
                } else if (mensagem.contains("Máximo de tentativas excedido")) {
                    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body(new EmailResponseDTO(false, "Número máximo de tentativas excedido", mensagem));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new EmailResponseDTO(false, "Código inválido", mensagem));
                }
            }
    }
}
