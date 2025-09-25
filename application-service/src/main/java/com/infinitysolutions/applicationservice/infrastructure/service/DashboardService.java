package com.infinitysolutions.applicationservice.infrastructure.service;

import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.dashboard.*;

import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoPedido;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final PedidoRepository pedidoRepository;

    
    public KpiResponseDTO getTipoEventoMaisPopular() {
        log.info("Buscando tipo de evento mais popular dos últimos 6 meses");
        
        LocalDateTime seiseMesesAtras = LocalDateTime.now().minusMonths(6);
        
        // Buscar todos os pedidos dos últimos 6 meses
        List<Object[]> resultados = pedidoRepository.findTipoEventoMaisPopular(seiseMesesAtras);
        
        if (resultados.isEmpty()) {
            log.warn("Nenhum pedido encontrado nos últimos 6 meses");
            return new KpiResponseDTO("Sem dados", 0L, 0.0);
        }
        
        // Calcular totais
        long totalPedidos = resultados.stream()
                .mapToLong(row -> (Long) row[1])
                .sum();
        
        // Pegar o tipo mais popular (primeiro resultado)
        Object[] maisPopular = resultados.get(0);
        TipoPedido tipo = (TipoPedido) maisPopular[0];
        Long quantidade = (Long) maisPopular[1];
        
        double percentual = totalPedidos > 0 ? (quantidade * 100.0) / totalPedidos : 0.0;
        
        log.info("Tipo mais popular: {} com {} pedidos ({}%)", tipo, quantidade, percentual);
        
        return new KpiResponseDTO(tipo.name(), quantidade, percentual);
    }

    
    public KpiResponseDTO getCategoriaMaisPopular() {
        log.info("Buscando categoria mais popular dos últimos 6 meses");
        
        LocalDateTime seiseMesesAtras = LocalDateTime.now().minusMonths(6);
        
        // Buscar categorias mais populares dos últimos 6 meses
        List<Object[]> resultados = pedidoRepository.findCategoriaMaisPopular(seiseMesesAtras);
        
        if (resultados.isEmpty()) {
            log.warn("Nenhuma categoria encontrada nos últimos 6 meses");
            return new KpiResponseDTO("Sem dados", 0L, 0.0);
        }
        
        // Calcular totais
        long totalProdutos = resultados.stream()
                .mapToLong(row -> (Long) row[1])
                .sum();
        
        // Pegar a categoria mais popular (primeiro resultado)
        Object[] maisPopular = resultados.get(0);
        String nomeCategoria = (String) maisPopular[0];
        Long quantidade = (Long) maisPopular[1];
        
        double percentual = totalProdutos > 0 ? (quantidade * 100.0) / totalProdutos : 0.0;
        
        log.info("Categoria mais popular: {} com {} produtos solicitados ({}%)", 
                nomeCategoria, quantidade, percentual);
        
        return new KpiResponseDTO(nomeCategoria, quantidade, percentual);
    }

    
    public TempoMedioAtendimentoDTO getTempoMedioAtendimento() {
        log.info("Calculando tempo médio de atendimento");
        
        // Buscar pedidos que foram aprovados (têm histórico de mudança de status)
        List<Object[]> resultados = pedidoRepository.findTempoMedioAtendimento();
        
        if (resultados.isEmpty()) {
            log.warn("Nenhum pedido aprovado encontrado para calcular tempo médio");
            return new TempoMedioAtendimentoDTO(0.0, 0L);
        }
        
        // Calcular a média dos tempos
        double mediaDias = resultados.stream()
                .mapToDouble(row -> {
                    LocalDateTime dataCriacao = (LocalDateTime) row[0];
                    LocalDateTime dataAprovacao = (LocalDateTime) row[1];
                    return ChronoUnit.DAYS.between(dataCriacao, dataAprovacao);
                })
                .average()
                .orElse(0.0);
        
        long totalPedidos = resultados.size();
        
        log.info("Tempo médio de atendimento: {} dias com base em {} pedidos", 
                mediaDias, totalPedidos);
        
        return new TempoMedioAtendimentoDTO(mediaDias, totalPedidos);
    }

    
    public List<PedidosPorMesDTO> getPedidosPorMes(int meses) {
        log.info("Buscando pedidos por mês dos últimos {} meses", meses);
        
        LocalDateTime dataInicio = LocalDateTime.now().minusMonths(meses);
        
        List<Object[]> resultados = pedidoRepository.findPedidosPorMes(dataInicio);
        
        return resultados.stream()
                .map(row -> {
                    Integer ano = (Integer) row[0];
                    Integer mes = (Integer) row[1];
                    Long quantidade = (Long) row[2];
                    
                    YearMonth yearMonth = YearMonth.of(ano, mes);
                    return new PedidosPorMesDTO(yearMonth, quantidade);
                })
                .sorted((a, b) -> a.mes().compareTo(b.mes()))
                .collect(Collectors.toList());
    }

    
    public List<EquipamentoPopularDTO> getEquipamentosPopulares(int limite) {
        log.info("Buscando {} equipamentos mais populares", limite);
        
        LocalDateTime seiseMesesAtras = LocalDateTime.now().minusMonths(6);
        
        List<Object[]> resultados = pedidoRepository.findEquipamentosPopulares(seiseMesesAtras, limite);
        
        if (resultados.isEmpty()) {
            log.warn("Nenhum equipamento encontrado");
            return List.of();
        }
        
        // Calcular total para percentuais
        long totalSolicitacoes = resultados.stream()
                .mapToLong(row -> (Long) row[1])
                .sum();
        
        return resultados.stream()
                .map(row -> {
                    String nomeEquipamento = (String) row[0];
                    Long quantidade = (Long) row[1];
                    double percentual = totalSolicitacoes > 0 ? (quantidade * 100.0) / totalSolicitacoes : 0.0;
                    
                    return new EquipamentoPopularDTO(nomeEquipamento, quantidade, percentual);
                })
                .collect(Collectors.toList());
    }

    
    public DashboardResponseDTO getDashboardCompleto() {
        log.info("Montando dashboard completo");
        
        // Executar todas as consultas em paralelo para melhor performance
        KpiResponseDTO tipoEvento = getTipoEventoMaisPopular();
        KpiResponseDTO categoria = getCategoriaMaisPopular();
        TempoMedioAtendimentoDTO tempoMedio = getTempoMedioAtendimento();
        List<PedidosPorMesDTO> pedidosPorMes = getPedidosPorMes(6);
        List<EquipamentoPopularDTO> equipamentos = getEquipamentosPopulares(10);
        
        return new DashboardResponseDTO(
                tipoEvento,
                categoria,
                tempoMedio,
                pedidosPorMes,
                equipamentos
        );
    }
}
