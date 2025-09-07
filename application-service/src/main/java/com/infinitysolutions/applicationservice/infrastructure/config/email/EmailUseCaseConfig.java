package com.infinitysolutions.applicationservice.infrastructure.config.email;

import com.infinitysolutions.applicationservice.core.domain.email.EmailTemplateBuilder;
import com.infinitysolutions.applicationservice.core.gateway.EmailGateway;
import com.infinitysolutions.applicationservice.core.usecases.email.*;
import com.infinitysolutions.applicationservice.infrastructure.gateway.CodigoAutenticacaoGatewayImpl;
import com.infinitysolutions.applicationservice.infrastructure.gateway.email.EmailConfigProviderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailUseCaseConfig {

    private final EmailGateway emailGateway;
    private final CodigoAutenticacaoGatewayImpl codigoAutenticacaoGateway;
    private final EmailConfigProviderImpl emailConfigProvider;

    public EmailUseCaseConfig(EmailGateway emailGateway, CodigoAutenticacaoGatewayImpl codigoAutenticacaoGateway, EmailConfigProviderImpl emailConfigProvider) {
        this.emailGateway = emailGateway;
        this.codigoAutenticacaoGateway = codigoAutenticacaoGateway;
        this.emailConfigProvider = emailConfigProvider;
    }

    @Bean
    public EmailTemplateBuilder emailTemplateBuilder() {
        return new EmailTemplateBuilder();
    }

    @Bean
    public EnviarEmailAutenticacao enviarEmailAutenticacao() {
        return new EnviarEmailAutenticacao(emailTemplateBuilder(), emailGateway, codigoAutenticacaoGateway);
    }

    @Bean
    public EnviarEmailCadastro enviarEmailCadastro() {
        return new EnviarEmailCadastro(emailGateway, emailTemplateBuilder());
    }

    @Bean
    public EnviarEmailCodigoResetSenha enviarEmailCodigoResetSenha() {
        return new EnviarEmailCodigoResetSenha(emailTemplateBuilder(), emailGateway);
    }

    @Bean
    public EnviarEmailConfirmacaoResetEmail enviarEmailConfirmacaoResetEmail() {
        return new EnviarEmailConfirmacaoResetEmail(emailTemplateBuilder(), emailGateway);
    }

    @Bean
    public EnviarEmailConfirmacaoResetSenha enviarEmailConfirmacaoResetSenha() {
        return new EnviarEmailConfirmacaoResetSenha(emailTemplateBuilder(), emailGateway);
    }

    @Bean
    public EnviarEmailNotificacaoCadastro enviarEmailNotificacaoCadastro() {
        return new EnviarEmailNotificacaoCadastro(emailTemplateBuilder(), emailGateway, emailConfigProvider);
    }

    @Bean
    public EnviarEmailNotificacaoMudancaStatusPedido enviarEmailNotificacaoMudancaStatusPedido() {
        return new EnviarEmailNotificacaoMudancaStatusPedido(emailTemplateBuilder(), emailGateway);
    }

    @Bean
    public EnviarEmailNotificacaoNovoPedido enviarEmailNotificacaoNovoPedido() {
        return new EnviarEmailNotificacaoNovoPedido(emailTemplateBuilder(), emailConfigProvider, emailGateway);
    }

    @Bean
    public EnviarEmailPedidoConcluidoUsuario enviarEmailPedidoConcluidoUsuario() {
        return new EnviarEmailPedidoConcluidoUsuario(emailTemplateBuilder(), emailGateway);
    }
}
