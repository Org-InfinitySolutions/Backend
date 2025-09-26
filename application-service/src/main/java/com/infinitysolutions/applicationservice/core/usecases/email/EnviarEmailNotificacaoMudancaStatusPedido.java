package com.infinitysolutions.applicationservice.core.usecases.email;

import com.infinitysolutions.applicationservice.core.domain.email.EmailTemplateBuilder;
import com.infinitysolutions.applicationservice.core.gateway.EmailGateway;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EmailNotificacaoMudancaStatus;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EnviarEmailResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EnviarEmailNotificacaoMudancaStatusPedido {

    private final EmailTemplateBuilder emailTemplateBuilder;
    private final EmailGateway emailGateway;

    public EnviarEmailNotificacaoMudancaStatusPedido(EmailTemplateBuilder emailTemplateBuilder, EmailGateway emailGateway) {
        this.emailTemplateBuilder = emailTemplateBuilder;
        this.emailGateway = emailGateway;
    }

    public EnviarEmailResponse execute(EmailNotificacaoMudancaStatus dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(emailTemplateBuilder.gerarPatternData());
        String dataAtualizacao = LocalDateTime.now().format(formatter);

        String conteudoEmail = emailTemplateBuilder.gerarTemplateMudancaStatusPedido(dto.nomeUsuario(), dto.numeroPedido(), dto.statusAnterior(), dto.novoStatus(), dataAtualizacao);

        String assunto = String.format("Status do Pedido #%s Atualizado - NovaLocações", dto.numeroPedido());

        emailGateway.enviarEmailHtmlAsync(dto.emailUsuario(), assunto, conteudoEmail);
        return new EnviarEmailResponse(true, String.format("Email de mudança de status do pedido #%s enviado com sucesso para: %s", dto.numeroPedido(), dto.emailUsuario()), null);

    }
}
