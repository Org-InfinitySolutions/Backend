package com.infinitysolutions.applicationservice.core.usecases.email;

import com.infinitysolutions.applicationservice.core.gateway.EmailGateway;
import com.infinitysolutions.applicationservice.core.domain.email.EmailTemplateBuilder;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EnviarEmailInput;

public class EnviarEmailCadastro {

    private final EmailGateway emailGateway;
    private final EmailTemplateBuilder emailTemplateBuilder;

    public EnviarEmailCadastro(EmailGateway emailGateway, EmailTemplateBuilder emailTemplateBuilder) {
        this.emailGateway = emailGateway;
        this.emailTemplateBuilder = emailTemplateBuilder;
    }

    public void execute(EnviarEmailInput dto) {
        String conteudoEmail = emailTemplateBuilder.gerarTemplateCadastroCompleto(dto.nome());
        emailGateway.enviarEmailHtmlAsync(dto.email(), "Cadastro Concluído! - NovaLocações", conteudoEmail);
    }
}