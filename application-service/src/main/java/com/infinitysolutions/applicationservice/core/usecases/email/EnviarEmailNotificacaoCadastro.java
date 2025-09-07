package com.infinitysolutions.applicationservice.core.usecases.email;

import com.infinitysolutions.applicationservice.core.gateway.EmailConfigProvider;
import com.infinitysolutions.applicationservice.core.gateway.EmailGateway;
import com.infinitysolutions.applicationservice.core.domain.email.EmailTemplateBuilder;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EnviarEmailInput;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class EnviarEmailNotificacaoCadastro {

    private final EmailTemplateBuilder emailTemplateBuilder;
    private final EmailGateway emailGateway;
    private final EmailConfigProvider emailConfigProvider;

    public EnviarEmailNotificacaoCadastro(EmailTemplateBuilder emailTemplateBuilder, EmailGateway emailGateway, EmailConfigProvider emailConfigProvider) {
        this.emailTemplateBuilder = emailTemplateBuilder;
        this.emailGateway = emailGateway;
        this.emailConfigProvider = emailConfigProvider;
    }

    public void execute(EnviarEmailInput dto) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(emailTemplateBuilder.gerarPatternData());
            String dataHoraFormatada = LocalDateTime.now().format(formatter);

            String conteudoEmail = emailTemplateBuilder.gerarTemplateNotificacaoNovoCadastro(dto.nome(), dto.email().getValor(), dataHoraFormatada);
            emailGateway.enviarEmailHtmlAsync(emailConfigProvider.getAdminEmail(), "Um Novo Cadastro Foi Concluído! - NovaLocações", conteudoEmail);
    }

}
