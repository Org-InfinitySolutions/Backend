package com.infinitysolutions.applicationservice.core.usecases.email;

import com.infinitysolutions.applicationservice.core.domain.email.EmailTemplateBuilder;
import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import com.infinitysolutions.applicationservice.core.gateway.EmailGateway;

public class EnviarEmailConfirmacaoResetSenha {

    private final EmailTemplateBuilder emailTemplateBuilder;
    private final EmailGateway emailGateway;

    public EnviarEmailConfirmacaoResetSenha(EmailTemplateBuilder emailTemplateBuilder, EmailGateway emailGateway) {
        this.emailTemplateBuilder = emailTemplateBuilder;
        this.emailGateway = emailGateway;
    }

    public void execute(Email emailDestinatario) {
        String assunto = "Sua senha foi alterada";
        String conteudo = emailTemplateBuilder.confirmacaoResetSenha();
        emailGateway.enviarEmailHtmlAsync(emailDestinatario, assunto, conteudo);
    }
}
