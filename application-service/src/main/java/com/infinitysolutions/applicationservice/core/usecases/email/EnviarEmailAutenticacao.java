package com.infinitysolutions.applicationservice.core.usecases.email;

import com.infinitysolutions.applicationservice.core.domain.email.EmailTemplateBuilder;
import com.infinitysolutions.applicationservice.core.gateway.CodigoAutenticacaoGateway;
import com.infinitysolutions.applicationservice.core.gateway.EmailGateway;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EnviarEmailInput;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EnviarEmailResponse;

public class EnviarEmailAutenticacao {

    private final EmailTemplateBuilder emailTemplateBuilder;
    private final EmailGateway emailGateway;
    private final CodigoAutenticacaoGateway codigoAutenticacaoGateway;

    public EnviarEmailAutenticacao(EmailTemplateBuilder emailTemplateBuilder, EmailGateway emailGateway, CodigoAutenticacaoGateway codigoAutenticacaoGateway) {
        this.emailTemplateBuilder = emailTemplateBuilder;
        this.emailGateway = emailGateway;
        this.codigoAutenticacaoGateway = codigoAutenticacaoGateway;
    }

    public EnviarEmailResponse execute(EnviarEmailInput dto) {

        String codigo = codigoAutenticacaoGateway.gerarCodigo(dto.email());
        String conteudoEmail = emailTemplateBuilder.gerarTemplateVerificacaoEmail(dto.nome(), codigo);
        emailGateway.enviarEmailHtmlAsync(dto.email(), "Código de Verificação - NovaLocações", conteudoEmail);
        return new EnviarEmailResponse(true, "Código de verificação enviado com sucesso para " + dto.email(), null);
    }
}
