package com.infinitysolutions.authservice.infra.configuration;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class LocalJavaMailSender {

    private final JavaMailSender mailSender;

    public void enviarEmailTextoSimples(String para, String assunto, String texto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(para);
            message.setSubject(assunto);
            message.setText(texto);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Erro ao enviar email {}", e.getMessage());
        }
    }


    public void enviarEmailHtml(String para, String assunto, String conteudoHtml) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(conteudoHtml, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Erro ao enviar email HTML {}", e.getMessage());
        }
    }

    @Async("emailTaskExecutor")
    public void enviarEmailHtmlAsync(String para, String assunto, String conteudoHtml) {
        try {
            log.info("Iniciando envio assíncrono de email HTML para: {}", para);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(conteudoHtml, true);
            mailSender.send(mimeMessage);
            log.info("Email HTML enviado com sucesso para: {}", para);
        } catch (Exception e) {
            log.error("Erro ao enviar email HTML para: {}", para, e);
        }
    }
}
