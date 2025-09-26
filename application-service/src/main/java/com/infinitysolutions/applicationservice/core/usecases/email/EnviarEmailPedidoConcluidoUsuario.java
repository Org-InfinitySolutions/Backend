package com.infinitysolutions.applicationservice.core.usecases.email;

import com.infinitysolutions.applicationservice.core.domain.email.EmailTemplateBuilder;
import com.infinitysolutions.applicationservice.core.domain.valueobject.SituacaoPedido;
import com.infinitysolutions.applicationservice.core.gateway.EmailGateway;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EmailNotificacaoPedidoConcluido;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EnviarEmailResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EnviarEmailPedidoConcluidoUsuario {

    private final EmailTemplateBuilder emailTemplateBuilder;
    private final EmailGateway emailGateway;

    public EnviarEmailPedidoConcluidoUsuario(EmailTemplateBuilder emailTemplateBuilder, EmailGateway emailGateway) {
        this.emailTemplateBuilder = emailTemplateBuilder;
        this.emailGateway = emailGateway;
    }

    public EnviarEmailResponse execute(EmailNotificacaoPedidoConcluido dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(emailTemplateBuilder.gerarPatternData());
        String dataHoraFormatada = LocalDateTime.now().format(formatter);

        String conteudoEmail = emailTemplateBuilder.gerarTemplatePedidoConcluido(dto.nomeUsuario(), dto.numeroPedido(), SituacaoPedido.EM_ANALISE.getNome(), dataHoraFormatada, dto.qtdItens());
        emailGateway.enviarEmailHtmlAsync(dto.emailUsuario(), "Pedido Concluído! - NovaLocações", conteudoEmail);
        return new EnviarEmailResponse(true, "Email de notificação de pedido enviado com sucesso!", null);
    }
}
