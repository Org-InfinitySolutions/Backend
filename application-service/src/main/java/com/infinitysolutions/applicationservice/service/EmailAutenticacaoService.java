package com.infinitysolutions.applicationservice.service;

import com.infinitysolutions.applicationservice.infra.exception.ErroInesperadoException;
import com.infinitysolutions.applicationservice.model.dto.email.EmailResponseDTO;
import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioAutenticacaoCadastroDTO;
import com.infinitysolutions.applicationservice.service.email.CodigoAutenticacaoService;
import com.infinitysolutions.applicationservice.service.email.EmailTemplateService;
import com.infinitysolutions.applicationservice.service.strategy.LocalJavaMailSenderStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailAutenticacaoService {

    private final CodigoAutenticacaoService codigoAutenticacaoService;
    private final EmailTemplateService emailTemplateService;
    private final LocalJavaMailSenderStrategy emailSender;

    private static final String EMAIL_ASSUNTO = "Código de Verificação - NovaLocações";

    public EmailResponseDTO enviarCodigoAutenticacao(UsuarioAutenticacaoCadastroDTO dto) {
        log.info("Iniciando envio de código de autenticação para: {}", dto.email());
        try {
            String codigo = codigoAutenticacaoService.gerarCodigo(dto.email());
            String conteudoEmail = emailTemplateService.gerarTemplateVerificacaoEmail(dto.nome(), codigo);
            emailSender.enviarEmailHtmlAsync(dto.email(), EMAIL_ASSUNTO, conteudoEmail);
            log.info("Código de autenticação enviado com sucesso para: {}", dto.email());
            return new EmailResponseDTO(true, "Código de verificação enviado com sucesso para " + dto.email(), null);
        } catch (Exception e) {
            log.error("Erro ao enviar código de autenticação para: {}", dto.email(), e);
            throw ErroInesperadoException.erroInesperado("Erro ao enviar código de verificação: ", e.getMessage());
        }
    }


    public Map.Entry<Boolean, String> validarCodigoAutenticacao(String email, String codigo) {
        log.info("Validando código de autenticação para: {}", email);
        try {
            var response = codigoAutenticacaoService.validarCodigo(email, codigo);
            if (response.valido()) {
                log.info("Código validado com sucesso para: {}", email);
            } else {
                log.warn("Tentativa de validação com código inválido para: {}", email);
            }
            return new AbstractMap.SimpleEntry<>(response.valido(), response.mensagem());
        } catch (Exception e) {
            log.error("Erro ao validar código de autenticação para: {}", email, e);
            throw ErroInesperadoException.erroInesperado("Erro ao validar código de verificação: ", e.getMessage());
        }
    }
}
