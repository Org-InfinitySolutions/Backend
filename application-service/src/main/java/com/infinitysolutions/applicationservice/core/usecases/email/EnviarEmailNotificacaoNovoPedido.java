package com.infinitysolutions.applicationservice.core.usecases.email;

import com.infinitysolutions.applicationservice.core.domain.email.EmailTemplateBuilder;
import com.infinitysolutions.applicationservice.core.domain.valueobject.SituacaoPedido;
import com.infinitysolutions.applicationservice.core.gateway.EmailConfigProvider;
import com.infinitysolutions.applicationservice.core.gateway.EmailGateway;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EmailPedidoConcluidoAdmin;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EnviarEmailResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EnviarEmailNotificacaoNovoPedido {

    private final EmailTemplateBuilder emailTemplateBuilder;
    private final EmailConfigProvider emailConfigProvider;
    private final EmailGateway emailGateway;

    public EnviarEmailNotificacaoNovoPedido(EmailTemplateBuilder emailTemplateBuilder, EmailConfigProvider emailConfigProvider, EmailGateway emailGateway) {
        this.emailTemplateBuilder = emailTemplateBuilder;
        this.emailConfigProvider = emailConfigProvider;
        this.emailGateway = emailGateway;
    }

    public EnviarEmailResponse execute(EmailPedidoConcluidoAdmin dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(emailTemplateBuilder.gerarPatternData());
        String dataHoraFormatada = LocalDateTime.now().format(formatter);

        String conteudoEmail = emailTemplateBuilder.gerarTemplateNotificacaoNovoPedido(dto.nomeUsuario(), dto.emailUsuario().getValor(), dto.telefoneUsuario(), dto.numeroPedido(), SituacaoPedido.EM_ANALISE.getNome(), dataHoraFormatada, dto.qtdItens(), dto.descricaoPedido());
        emailGateway.enviarEmailHtmlAsync(emailConfigProvider.getAdminEmail(), "Um Novo Pedido foi Concluído! - NovaLocações", conteudoEmail);
        return new EnviarEmailResponse(true, "Email de notificação de pedido para o administrador enviado com sucesso!", null);

    }
}
