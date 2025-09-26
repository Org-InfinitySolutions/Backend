package com.infinitysolutions.applicationservice.infrastructure.gateway.email;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import com.infinitysolutions.applicationservice.core.gateway.EmailGateway;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EnviarEmailGatewayImpl implements EmailGateway {

    private final JavaMailSender mailSender;

    @Override
    @Async("emailTaskExecutor")
    public void enviarEmailHtmlAsync(Email email, String assunto, String conteudoEmail) {
        try {
            log.info("Iniciando envio assíncrono de email HTML para: {}", email.getValor());
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email.getValor());
            helper.setSubject(assunto);
            helper.setText(conteudoEmail, true);
            mailSender.send(mimeMessage);
            log.info("Email HTML enviado com sucesso para {}", email.getValor());
        } catch (Exception e) {
            log.error("Erro ao enviar email HTML para: {}", email.getValor(), e);
        }
    }
}
