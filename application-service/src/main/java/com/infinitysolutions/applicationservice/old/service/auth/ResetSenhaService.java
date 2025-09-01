package com.infinitysolutions.applicationservice.old.service.auth;

import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth.RespostaResetSenha;
import com.infinitysolutions.applicationservice.old.service.email.CodigoAutenticacaoService;
import com.infinitysolutions.applicationservice.old.service.email.EnvioEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResetSenhaService {
    
    private final CredencialService credencialService;
    private final CodigoAutenticacaoService codigoAutenticacaoService;
    private final EnvioEmailService envioEmailService;
    
    public RespostaResetSenha enviarCodigoResetSenha(String email) {
        log.info("Iniciando processo de reset de senha para email: {}", email);
        
        try {
            if (!credencialService.verificarEmailExiste(email)) {
                log.warn("Tentativa de reset de senha para email não cadastrado: {}", email);
                return new RespostaResetSenha(true, "Se o email estiver cadastrado, você receberá um código para reset de senha");
            }
            
            String codigo = codigoAutenticacaoService.gerarCodigo(email);            
            String nomeUsuario = "Usuário"; // Mudar depois se precisar.
            
            envioEmailService.enviarCodigoResetSenha(email, nomeUsuario, codigo);
            
            log.info("Código de reset de senha enviado com sucesso para: {}", email);
            return new RespostaResetSenha(true, "Código de reset de senha enviado com sucesso para o email fornecido");
            
        } catch (Exception e) {
            log.error("Erro ao enviar código de reset de senha para: {}", email, e);
            return new RespostaResetSenha(false, "Erro interno. Tente novamente mais tarde");
        }
    }
    
    @Transactional
    public RespostaResetSenha resetarSenhaComCodigo(String email, String codigo, String novaSenha) {
        log.info("Validando código e alterando senha para email: {}", email);
        
        try {
            Map.Entry<Boolean, String> validacao = codigoAutenticacaoService.validarCodigoAutenticacao(email, codigo);
            
            if (!validacao.getKey()) {
                log.warn("Código inválido ou expirado para email: {}", email);
                String mensagem = validacao.getValue();
                
                if (mensagem.contains("expirado")) {
                    return new RespostaResetSenha(false, "Código expirado. Solicite um novo código");
                } else if (mensagem.contains("excedido")) {
                    return new RespostaResetSenha(false, "Número máximo de tentativas excedido. Solicite um novo código");
                } else {
                    return new RespostaResetSenha(false, "Código inválido");
                }
            }
            credencialService.resetarSenhaPorEmail(email, novaSenha);            
            log.info("Senha alterada com sucesso para email: {}", email);
            return new RespostaResetSenha(true, "Senha alterada com sucesso");
            
        } catch (RecursoNaoEncontradoException e) {
            log.warn("Tentativa de reset para email não encontrado: {}", email);
            return new RespostaResetSenha(false, "Email não encontrado");
        } catch (Exception e) {
            log.error("Erro ao resetar senha para email: {}", email, e);
            return new RespostaResetSenha(false, "Erro interno. Tente novamente mais tarde");
        }
    }
}
