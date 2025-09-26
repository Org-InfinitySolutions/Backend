package com.infinitysolutions.applicationservice.infrastructure.controller;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import com.infinitysolutions.applicationservice.core.gateway.CodigoAutenticacaoGateway;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailAutenticacao;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth.UsuarioAutenticacaoValidarCodigoDTO;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EnviarEmailInput;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EnviarEmailResponse;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.email.EnviarEmailDto;
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

    private final EnviarEmailAutenticacao enviarCodigoAutenticacaoCase;
    private final CodigoAutenticacaoGateway codigoAutenticacaoGateway;

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
    public EnviarEmailResponse enviarCodigoVerificacao(@RequestBody @Valid EnviarEmailDto dto) {
            return enviarCodigoAutenticacaoCase.execute(new EnviarEmailInput(dto.getNome(), Email.of(dto.getEmail())));
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
    public ResponseEntity<EnviarEmailResponse> validarCodigo(@RequestBody @Valid UsuarioAutenticacaoValidarCodigoDTO dto) {
            var response = codigoAutenticacaoGateway.validarCodigoAutenticacao(Email.of(dto.email()), dto.codigo());
            if (response.getKey()) {
                return ResponseEntity.ok(
                    new EnviarEmailResponse(true, "Código validado com sucesso", response.getValue())
                );
            } else {
                String mensagem = response.getValue();
                if (mensagem.contains("sem código ativo")) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new EnviarEmailResponse(false, "Código não encontrado para o email informado", mensagem));
                } else if (mensagem.contains("Código expirado")) {
                    return ResponseEntity.status(HttpStatus.GONE)
                        .body(new EnviarEmailResponse(false, "Código expirado", mensagem));
                } else if (mensagem.contains("Máximo de tentativas excedido")) {
                    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body(new EnviarEmailResponse(false, "Número máximo de tentativas excedido", mensagem));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new EnviarEmailResponse(false, "Código inválido", mensagem));
                }
            }
    }
}
