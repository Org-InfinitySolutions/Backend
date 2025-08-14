package com.infinitysolutions.authservice.service;

import com.infinitysolutions.authservice.infra.configuration.EmailTemplate;
import com.infinitysolutions.authservice.infra.configuration.LocalJavaMailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EnvioEmailService {

    private final LocalJavaMailSender localJavaMailSender;
    private final EmailTemplate emailTemplate;


    public void enviarConfirmacaoResetEmail(String emailDestinatario) {
        String assunto = "Novo email vinculado";
        String conteudo = emailTemplate.confirmacaoResetEmail(emailDestinatario);
        localJavaMailSender.enviarEmailHtmlAsync(emailDestinatario, assunto, conteudo);
    }

    public void enviarConfirmacaoResetSenha(String emailDestinatario) {
        String assunto = "Sua senha foi alterada";
        String conteudo = emailTemplate.confirmacaoResetSenha();
        localJavaMailSender.enviarEmailHtmlAsync(emailDestinatario, assunto, conteudo);
    }

    public void enviarCodigoResetSenha(String emailDestinatario, String nome, String codigo) {
        String assunto = "Código para Reset de Senha - NovaLocações";
        String conteudo = emailTemplate.gerarTemplateResetSenha(nome, codigo);
        localJavaMailSender.enviarEmailHtmlAsync(emailDestinatario, assunto, conteudo);
    }
}
