package com.infinitysolutions.applicationservice.core.usecases.email;

import com.infinitysolutions.applicationservice.core.domain.email.EmailTemplateBuilder;
import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import com.infinitysolutions.applicationservice.core.gateway.EmailGateway;

public class EnviarEmailConfirmacaoResetEmail {

    private final EmailTemplateBuilder emailTemplateBuilder;
    private final EmailGateway emailGateway;

    public EnviarEmailConfirmacaoResetEmail(EmailTemplateBuilder emailTemplateBuilder, EmailGateway emailGateway) {
        this.emailTemplateBuilder = emailTemplateBuilder;
        this.emailGateway = emailGateway;
    }

    public void execute(Email emailDestinatario) {
        String assunto = "Novo email vinculado";
        String conteudo = emailTemplateBuilder.confirmacaoResetEmail(emailDestinatario.getValor());
        emailGateway.enviarEmailHtmlAsync(emailDestinatario, assunto, conteudo);
    }
}
