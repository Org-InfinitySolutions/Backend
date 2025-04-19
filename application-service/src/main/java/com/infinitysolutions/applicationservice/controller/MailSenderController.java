package com.infinitysolutions.applicationservice.controller;

import com.infinitysolutions.applicationservice.model.dto.auth.UsuarioAutenticacaoValidarCodigoDTO;
import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioAutenticacaoCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.email.EmailResponseDTO;
import com.infinitysolutions.applicationservice.service.EmailAutenticacaoService;
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

    private final EmailAutenticacaoService emailAutenticacaoService;

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
        @ApiResponse(responseCode = "200", description = "Solicitação processada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @ResponseStatus(HttpStatus.OK)
    public EmailResponseDTO validarCodigo(@RequestBody @Valid UsuarioAutenticacaoValidarCodigoDTO dto) {
            var response = emailAutenticacaoService.validarCodigoAutenticacao(dto.email(), dto.codigo());
            if (response.getKey()) {
                return new EmailResponseDTO(true, "Código validado com sucesso", response.getValue());
            } else {
                return new EmailResponseDTO(false, "Falha na validação do código.", response.getValue());
            }
    }
}
