package com.infinitysolutions.applicationservice.core.usecases.email;

import com.infinitysolutions.applicationservice.core.gateway.ProjectConfigProvider;
import com.infinitysolutions.applicationservice.core.gateway.EmailGateway;
import com.infinitysolutions.applicationservice.core.domain.email.EmailTemplateBuilder;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EnviarEmailInput;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class EnviarEmailNotificacaoCadastro {

    private final EmailTemplateBuilder emailTemplateBuilder;
    private final EmailGateway emailGateway;
    private final ProjectConfigProvider projectConfigProvider;

    public EnviarEmailNotificacaoCadastro(EmailTemplateBuilder emailTemplateBuilder, EmailGateway emailGateway, ProjectConfigProvider projectConfigProvider) {
        this.emailTemplateBuilder = emailTemplateBuilder;
        this.emailGateway = emailGateway;
        this.projectConfigProvider = projectConfigProvider;
    }

    public void execute(EnviarEmailInput dto) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(emailTemplateBuilder.gerarPatternData());
            String dataHoraFormatada = LocalDateTime.now().format(formatter);

            String conteudoEmail = emailTemplateBuilder.gerarTemplateNotificacaoNovoCadastro(dto.nome(), dto.email().getValor(), dataHoraFormatada);
            emailGateway.enviarEmailHtmlAsync(projectConfigProvider.getAdminEmail(), "Um Novo Cadastro Foi Concluído! - NovaLocações", conteudoEmail);
    }

}
