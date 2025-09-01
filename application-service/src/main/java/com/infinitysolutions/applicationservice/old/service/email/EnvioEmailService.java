package com.infinitysolutions.applicationservice.old.service.email;

import com.infinitysolutions.applicationservice.old.infra.exception.ErroInesperadoException;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.email.EmailNotificacaoMudancaStatusDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.email.EmailPedidoConcluidoAdminDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.email.EmailNotificacaoPedidoConcluidoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.email.EmailResponseDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioAutenticacaoCadastroDTO;
import com.infinitysolutions.applicationservice.core.domain.valueobject.SituacaoPedido;
import com.infinitysolutions.applicationservice.old.service.strategy.LocalJavaMailSenderStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
@Slf4j
public class EnvioEmailService {

    private final CodigoAutenticacaoService codigoAutenticacaoService;
    private final EmailTemplateService emailTemplateService;
    private final LocalJavaMailSenderStrategy emailSender;
    private static final String PATTERN_DATA = "dd/MM/yyyy - HH:mm";
    private final LocalJavaMailSenderStrategy localJavaMailSenderStrategy;

    @Value("${services.admin.email}")
    private String adminEmail;

    public EmailResponseDTO enviarCodigoAutenticacao(UsuarioAutenticacaoCadastroDTO dto) {
        log.info("Iniciando envio de código de autenticação para: {}", dto.email());
        try {
            String codigo = codigoAutenticacaoService.gerarCodigo(dto.email());
            String conteudoEmail = emailTemplateService.gerarTemplateVerificacaoEmail(dto.nome(), codigo);
            emailSender.enviarEmailHtmlAsync(dto.email(), "Código de Verificação - NovaLocações", conteudoEmail);
            log.info("Código de autenticação enviado com sucesso para: {}", dto.email());
            return new EmailResponseDTO(true, "Código de verificação enviado com sucesso para " + dto.email(), null);
        } catch (Exception e) {
            log.error("Erro ao enviar código de autenticação para: {}", dto.email(), e);
            throw ErroInesperadoException.erroInesperado("Erro ao enviar código de verificação: ", e.getMessage());
        }
    }

    public EmailResponseDTO enviarCadastroSucessoUsuario(UsuarioAutenticacaoCadastroDTO dto) {
        try {
            String conteudoEmail = emailTemplateService.gerarTemplateCadastroCompleto(dto.nome());
            emailSender.enviarEmailHtmlAsync(dto.email(), "Cadastro Concluído! - NovaLocações", conteudoEmail);
            log.info("Email de notificação de cadastro enviado com sucesso para: {}", dto.email());
            return new EmailResponseDTO(true, "Email de notificação de cadastro enviado com sucesso para: " + dto.email(), null);
        } catch (Exception e) {
            log.error("Erro ao enviar notificação de sucesso de cadastro para: {}", dto.email(), e);
            throw ErroInesperadoException.erroInesperado("Erro ao enviar notificação de sucesso de cadastro: ", e.getMessage());        }
    }    public EmailResponseDTO enviarNotificacaoNovoCadastro(UsuarioAutenticacaoCadastroDTO dto) {
        try {
            // Formatando a data no padrão brasileiro: dd/MM/yyyy HH:mm
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_DATA);
            String dataHoraFormatada = LocalDateTime.now().format(formatter);
            
            String conteudoEmail = emailTemplateService.gerarTemplateNotificacaoNovoCadastro(dto.nome(), dto.email(), dataHoraFormatada);
            emailSender.enviarEmailHtmlAsync(adminEmail, "Um Novo Cadastro Foi Concluído! - NovaLocações", conteudoEmail);
            log.info("Email de notificação de cadastro enviado com sucesso para o administrador: {}", adminEmail);
            return new EmailResponseDTO(true, "Email de notificação de cadastro enviado com sucesso para o administrador", null);
        } catch (Exception e) {
            log.error("Erro ao enviar notificação de sucesso de cadastro para o administrador", e);
            throw ErroInesperadoException.erroInesperado("Erro ao enviar notificação de sucesso de cadastro para o administrador: ", e.getMessage());
        }
    }


    public EmailResponseDTO enviarPedidoConcluidoUsuario(EmailNotificacaoPedidoConcluidoDTO dto) {
        try {
            // Formatando a data no padrão brasileiro: dd/MM/yyyy HH:mm
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_DATA);
            String dataHoraFormatada = LocalDateTime.now().format(formatter);

            String conteudoEmail = emailTemplateService.gerarTemplatePedidoConcluido(dto.nomeUsuario(), dto.numeroPedido(), SituacaoPedido.EM_ANALISE.getNome(), dataHoraFormatada, dto.qtdItens());
            emailSender.enviarEmailHtmlAsync(dto.emailUsuario(), "Pedido Concluído! - NovaLocações", conteudoEmail);
            log.info("Email de notificação de pedido enviado com sucesso!");
            return new EmailResponseDTO(true, "Email de notificação de pedido enviado com sucesso!", null);
        } catch (Exception e) {
            log.error("Erro ao enviar notificação de pedido", e);
            throw ErroInesperadoException.erroInesperado("Erro ao enviar notificação de sucesso de pedido: ", e.getMessage());
        }
    }
    public EmailResponseDTO enviarNotificacaoNovoPedido(EmailPedidoConcluidoAdminDTO dto) {
        try {
            // Formatando a data no padrão brasileiro: dd/MM/yyyy HH:mm
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_DATA);
            String dataHoraFormatada = LocalDateTime.now().format(formatter);

            String conteudoEmail = emailTemplateService.gerarTemplateNotificacaoNovoPedido(dto.nomeUsuario(), dto.emailUsuario(),dto.telefoneUsuario(), dto.numeroPedido(), SituacaoPedido.EM_ANALISE.getNome(), dataHoraFormatada, dto.qtdItens(), dto.descricaoPedido());
            emailSender.enviarEmailHtmlAsync(adminEmail, "Um Novo Pedido foi Concluído! - NovaLocações", conteudoEmail);
            log.info("Email de notificação de pedido para o administrador enviado com sucesso!");
            return new EmailResponseDTO(true, "Email de notificação de pedido para o administrador enviado com sucesso!", null);
        } catch (Exception e) {
            log.error("Erro ao enviar notificação de pedido", e);
            throw ErroInesperadoException.erroInesperado("Erro ao enviar notificação de sucesso de pedido para o administrador: ", e.getMessage());
        }
    }



    public EmailResponseDTO enviarNotificacaoMudancaStatusPedido(EmailNotificacaoMudancaStatusDTO dto) {
        try {
            // Formatando a data no padrão brasileiro: dd/MM/yyyy HH:mm
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_DATA);
            String dataAtualizacao = LocalDateTime.now().format(formatter);

            String conteudoEmail = emailTemplateService.gerarTemplateMudancaStatusPedido(dto.nomeUsuario(), dto.numeroPedido(), dto.statusAnterior(), dto.novoStatus(), dataAtualizacao);

            String assunto = String.format("Status do Pedido #%s Atualizado - NovaLocações", dto.numeroPedido());

            emailSender.enviarEmailHtmlAsync(dto.emailUsuario(), assunto, conteudoEmail);
            log.info("Email de mudança de status do pedido #{} enviado com sucesso para: {}", dto.numeroPedido(), dto.emailUsuario());
            return new EmailResponseDTO(true, String.format("Email de mudança de status do pedido #%s enviado com sucesso para: %s", dto.numeroPedido(), dto.emailUsuario()), null);
        } catch (Exception e) {
            log.error("Erro ao enviar notificação de mudança de status do pedido #{} para: {}", dto.numeroPedido(), dto.emailUsuario(), e);
            throw ErroInesperadoException.erroInesperado("Erro ao enviar notificação de mudança de status do pedido: ", e.getMessage());
        }
    }

    public void enviarConfirmacaoResetEmail(String emailDestinatario) {
        String assunto = "Novo email vinculado";
        String conteudo = emailTemplateService.confirmacaoResetEmail(emailDestinatario);
        localJavaMailSenderStrategy.enviarEmailHtmlAsync(emailDestinatario, assunto, conteudo);
    }

    public void enviarConfirmacaoResetSenha(String emailDestinatario) {
        String assunto = "Sua senha foi alterada";
        String conteudo = emailTemplateService.confirmacaoResetSenha();
        localJavaMailSenderStrategy.enviarEmailHtmlAsync(emailDestinatario, assunto, conteudo);
    }

    public void enviarCodigoResetSenha(String emailDestinatario, String nome, String codigo) {
        String assunto = "Código para Reset de Senha - NovaLocações";
        String conteudo = emailTemplateService.gerarTemplateResetSenha(nome, codigo);
        localJavaMailSenderStrategy.enviarEmailHtmlAsync(emailDestinatario, assunto, conteudo);
    }

}
