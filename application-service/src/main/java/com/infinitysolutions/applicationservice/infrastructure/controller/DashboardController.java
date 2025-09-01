package com.infinitysolutions.applicationservice.infrastructure.controller;

import com.infinitysolutions.applicationservice.old.infra.utils.AuthenticationUtils;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.dashboard.*;
import com.infinitysolutions.applicationservice.old.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(
    name = "Dashboard", 
    description = "API para dados de dashboard administrativo. " +
                 "Fornece KPIs, métricas e dados para gráficos do painel administrativo."
)
public class DashboardController {

    private final DashboardService dashboardService;
    private final AuthenticationUtils authenticationUtils;

    private void validateAdminAccess(Authentication authentication) {
        if (!authenticationUtils.isAuthenticated(authentication)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }
        
        if (!authenticationUtils.isAdminOrEmployee(authentication)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado. Apenas administradores e funcionários podem acessar o dashboard");
        }
    }

    @GetMapping("/kpi/tipo-evento-popular")
    @Operation(
        summary = "KPI - Tipo de evento mais procurado",
        description = "Retorna qual tipo de evento (INDOOR ou OUTDOOR) foi mais procurado nos últimos 6 meses"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "KPI retornado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<KpiResponseDTO> getTipoEventoMaisPopular(
            @Parameter(description = "Informações de autenticação do usuário", hidden = true)
            Authentication authentication) {
        validateAdminAccess(authentication);
        KpiResponseDTO kpi = dashboardService.getTipoEventoMaisPopular();
        return ResponseEntity.ok(kpi);
    }

    @GetMapping("/kpi/categoria-popular")
    @Operation(
        summary = "KPI - Categoria mais procurada", 
        description = "Retorna qual categoria de produto foi mais procurada nos últimos 6 meses"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "KPI retornado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<KpiResponseDTO> getCategoriaMaisPopular(
            @Parameter(description = "Informações de autenticação do usuário", hidden = true)
            Authentication authentication) {
        validateAdminAccess(authentication);
        KpiResponseDTO kpi = dashboardService.getCategoriaMaisPopular();
        return ResponseEntity.ok(kpi);
    }

    @GetMapping("/kpi/tempo-medio-atendimento")
    @Operation(
        summary = "KPI - Tempo médio de atendimento",
        description = "Retorna o tempo médio para um pedido sair de 'Em Análise' para 'Aprovado'"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tempo médio calculado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<TempoMedioAtendimentoDTO> getTempoMedioAtendimento(
            @Parameter(description = "Informações de autenticação do usuário", hidden = true)
            Authentication authentication) {
        validateAdminAccess(authentication);
        TempoMedioAtendimentoDTO tempo = dashboardService.getTempoMedioAtendimento();
        return ResponseEntity.ok(tempo);
    }

    @GetMapping("/grafico/pedidos-por-mes")
    @Operation(
        summary = "Gráfico de linha - Pedidos por mês",
        description = "Retorna dados para gráfico de linha mostrando a quantidade de pedidos nos últimos 6 meses"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados do gráfico retornados com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<List<PedidosPorMesDTO>> getPedidosPorMes(
            @Parameter(description = "Número de meses para buscar (padrão: 6)")
            @RequestParam(defaultValue = "6") int meses,
            @Parameter(description = "Informações de autenticação do usuário", hidden = true)
            Authentication authentication) {
        validateAdminAccess(authentication);
        List<PedidosPorMesDTO> dados = dashboardService.getPedidosPorMes(meses);
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/grafico/equipamentos-populares")
    @Operation(
        summary = "Gráfico pizza - Equipamentos mais procurados",
        description = "Retorna dados para gráfico pizza mostrando os equipamentos/produtos mais solicitados"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados do gráfico retornados com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<List<EquipamentoPopularDTO>> getEquipamentosPopulares(
            @Parameter(description = "Número máximo de equipamentos para retornar (padrão: 10)")
            @RequestParam(defaultValue = "10") int limite,
            @Parameter(description = "Informações de autenticação do usuário", hidden = true)
            Authentication authentication) {
        validateAdminAccess(authentication);
        List<EquipamentoPopularDTO> dados = dashboardService.getEquipamentosPopulares(limite);
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/completo")
    @Operation(
        summary = "Dashboard completo",
        description = "Retorna todos os dados do dashboard em uma única chamada para otimizar performance"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dashboard completo retornado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<DashboardResponseDTO> getDashboardCompleto(
            @Parameter(description = "Informações de autenticação do usuário", hidden = true)
            Authentication authentication) {
        validateAdminAccess(authentication);
        DashboardResponseDTO dashboard = dashboardService.getDashboardCompleto();
        return ResponseEntity.ok(dashboard);
    }
}
