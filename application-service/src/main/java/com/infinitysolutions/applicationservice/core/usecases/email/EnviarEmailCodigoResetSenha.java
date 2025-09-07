package com.infinitysolutions.applicationservice.core.usecases.email;

import com.infinitysolutions.applicationservice.core.domain.email.EmailTemplateBuilder;
import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import com.infinitysolutions.applicationservice.core.gateway.EmailGateway;

public class EnviarEmailCodigoResetSenha {

    private final EmailTemplateBuilder emailTemplateBuilder;
    private final EmailGateway emailGateway;

    public EnviarEmailCodigoResetSenha(EmailTemplateBuilder emailTemplateBuilder, EmailGateway emailGateway) {
        this.emailTemplateBuilder = emailTemplateBuilder;
        this.emailGateway = emailGateway;
    }

    public void execute(Email emailDestinatario, String nome, String codigo) {
        String assunto = "Código para Reset de Senha - NovaLocações";
        String conteudo = emailTemplateBuilder.gerarTemplateResetSenha(nome, codigo);
        emailGateway.enviarEmailHtmlAsync(emailDestinatario, assunto, conteudo);
    }
}
