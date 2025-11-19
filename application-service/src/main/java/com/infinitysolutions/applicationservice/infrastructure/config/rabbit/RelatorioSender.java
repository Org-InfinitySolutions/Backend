package com.infinitysolutions.applicationservice.infrastructure.config.rabbit;

import com.infinitysolutions.applicationservice.core.domain.valueobject.SituacaoPedido;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoPedido;
import com.infinitysolutions.applicationservice.core.gateway.ProjectConfigProvider;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.PedidoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

@Service
@Slf4j
public class RelatorioSender {

    private final RabbitTemplate rabbitTemplate;
    private final PedidoRepository pedidoRepository;
    private final ProjectConfigProvider projectConfigProvider;

    @Value("${services.admin.email}")
    private String adminEmail;

    public RelatorioSender(RabbitTemplate rabbitTemplate, PedidoRepository pedidoRepository, ProjectConfigProvider projectConfigProvider) {
        this.rabbitTemplate = rabbitTemplate;
        this.pedidoRepository = pedidoRepository;
        this.projectConfigProvider = projectConfigProvider;
    }

    /**
     * Executa no primeiro dia de cada mês, às 08:00:00.
     * Expressão Cron:
     * - 0: segundos
     * - 0: minutos
     * - 8: hora (8 da manhã)
     * - 1: dia do mês (primeiro dia)
     * - *: mês (todo mês)
     * - *: dia da semana (qualquer)
     */
    @Scheduled(cron = "0 0 8 1 * *", zone = "America/Sao_Paulo")
    public void gerarRelatorioMensal() {
        log.info("Iniciando geração do relatório mensal às: {}", LocalDateTime.now());

        try {
            MensagemRelatorioDTO relatorio = recolherDados();
            enviarRelatorio(relatorio);
            log.info("Relatório mensal enviado com sucesso para a fila RabbitMQ");
        } catch (Exception e) {
            log.error("Erro ao gerar e enviar relatório mensal: {}", e.getMessage(), e);
        }
    }

    /**
     * Coleta os dados do mês anterior para gerar o relatório
     */
    public MensagemRelatorioDTO recolherDados() {
        LocalDateTime agora = LocalDateTime.now();
        
        // Calcular primeiro e último dia do mês anterior
        LocalDateTime inicioMesAnterior = agora.minusMonths(1)
                .with(TemporalAdjusters.firstDayOfMonth())
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
                
        LocalDateTime fimMesAnterior = agora.minusMonths(1)
                .with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        log.info("Coletando dados do período: {} até {}", inicioMesAnterior, fimMesAnterior);

        String mesAno = inicioMesAnterior.format(DateTimeFormatter.ofPattern("MM/yyyy"));

        Long totalPedidos = pedidoRepository.countPedidosByPeriodo(inicioMesAnterior, fimMesAnterior);
        
        Long pedidosEmAnalise = pedidoRepository.countPedidosBySituacaoAndPeriodo(
                inicioMesAnterior, fimMesAnterior, SituacaoPedido.EM_ANALISE);
                
        Long pedidosAprovados = pedidoRepository.countPedidosBySituacaoAndPeriodo(
                inicioMesAnterior, fimMesAnterior, SituacaoPedido.APROVADO);
                
        Long pedidosEmEvento = pedidoRepository.countPedidosBySituacaoAndPeriodo(
                inicioMesAnterior, fimMesAnterior, SituacaoPedido.EM_EVENTO);
                
        Long pedidosFinalizados = pedidoRepository.countPedidosBySituacaoAndPeriodo(
                inicioMesAnterior, fimMesAnterior, SituacaoPedido.FINALIZADO);
                
        Long pedidosCancelados = pedidoRepository.countPedidosBySituacaoAndPeriodo(
                inicioMesAnterior, fimMesAnterior, SituacaoPedido.CANCELADO);
                
        Long pedidosIndoor = pedidoRepository.countPedidosByTipoAndPeriodo(
                inicioMesAnterior, fimMesAnterior, TipoPedido.INDOOR);
                
        Long pedidosOutdoor = pedidoRepository.countPedidosByTipoAndPeriodo(
                inicioMesAnterior, fimMesAnterior, TipoPedido.OUTDOOR);
                
        Long totalItensLocados = pedidoRepository.countTotalItensLocadosByPeriodo(
                inicioMesAnterior, fimMesAnterior);

        log.info("Dados coletados - Total pedidos: {}, Em análise: {}, Aprovados: {}, Em evento: {}, Finalizados: {}", 
                totalPedidos, pedidosEmAnalise, pedidosAprovados, pedidosEmEvento, pedidosFinalizados);


        
        return new MensagemRelatorioDTO(
                adminEmail,
                "",
                new RelatorioDTO(
                mesAno,
                totalPedidos.intValue(),
                pedidosEmAnalise.intValue(),
                pedidosAprovados.intValue(),
                pedidosEmEvento.intValue(),
                pedidosFinalizados.intValue(),
                pedidosCancelados.intValue(),
                pedidosIndoor.intValue(),
                pedidosOutdoor.intValue(),
                totalItensLocados.intValue(),
                LocalDateTime.now()
        )
        );
    }

    public void enviarRelatorio(MensagemRelatorioDTO relatorio) {
        log.info("Enviando relatório para RabbitMQ: {}", relatorio);
        rabbitTemplate.convertAndSend("relatorio.fanout.exchange", "", relatorio);
        
        log.info("Relatório mensal enviado para a fila. Destinatário admin: {}", relatorio.email());
    }


}
