package com.infinitysolutions.applicationservice.infrastructure.controller;

import com.infinitysolutions.applicationservice.core.exception.AutenticacaoException;
import com.infinitysolutions.applicationservice.infrastructure.config.rabbit.MensagemRelatorioDTO;
import com.infinitysolutions.applicationservice.infrastructure.config.rabbit.RelatorioDTO;
import com.infinitysolutions.applicationservice.infrastructure.config.rabbit.RelatorioSender;
import com.infinitysolutions.applicationservice.infrastructure.utils.AuthenticationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Relatórios", description = "Endpoints para gerenciamento de relatórios do sistema")
public class RelatorioController {

    private final RelatorioSender relatorioSender;
    private final AuthenticationUtils authenticationUtils;

    @PostMapping("/mensal/gerar")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Gerar relatório mensal manualmente",
            description = "Gera e envia um relatório mensal dos pedidos para a fila RabbitMQ. " +
                         "Este endpoint é para testes e uso administrativo. O relatório é gerado automaticamente " +
                         "todo primeiro dia do mês às 8h00."
    )
    public ResponseEntity<String> gerarRelatorioMensal(Authentication authentication) {
        if (!authenticationUtils.isAdmin(authentication)) throw AutenticacaoException.acessoNegado();
        try {
            log.info("Solicitação manual de geração de relatório mensal");
            MensagemRelatorioDTO relatorio = relatorioSender.recolherDados();
            relatorioSender.enviarRelatorio(relatorio);
            
            return ResponseEntity.ok("Relatório mensal gerado e enviado com sucesso para a fila RabbitMQ. " +
                                   "Destinatário: " + relatorio.email());
        } catch (Exception e) {
            log.error("Erro ao gerar relatório mensal manualmente: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar relatório: " + e.getMessage());
        }
    }
}
