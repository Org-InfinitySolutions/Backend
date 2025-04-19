package com.infinitysolutions.applicationservice.service.email;

import com.infinitysolutions.applicationservice.infra.validation.EmailValido;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class CodigoAutenticacaoService {

    private static final int CODIGO_TAMANHO = 6;
    private static final long CODIGO_VALIDADE_MINUTOS = 10;
    private static final int MAX_TENTATIVAS = 3;
    
    private final Map<String, CodigoAutenticacao> codigosAtivos = new ConcurrentHashMap<>();
    private final Map<String, Integer> tentativasFalhas = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    public String gerarCodigo(@EmailValido String email) {
        String emailNormalizado = email.toLowerCase().trim();
        String codigo = gerarCodigoAleatorio();
        LocalDateTime expiracao = LocalDateTime.now().plusMinutes(CODIGO_VALIDADE_MINUTOS);
        tentativasFalhas.remove(emailNormalizado);
        
        codigosAtivos.put(emailNormalizado, new CodigoAutenticacao(codigo, expiracao, 0));
        log.debug("Código de verificação gerado para: {}, expira em: {}", emailNormalizado, expiracao);
        return codigo;
    }

    public AutenticacaoResposta validarCodigo(@EmailValido String email, @NotNull String codigo) {
        String emailNormalizado = email.toLowerCase().trim();
        CodigoAutenticacao codigoArmazenado = codigosAtivos.get(emailNormalizado);

        if (codigoArmazenado == null) {
            final String mensagem = "Tentativa de validação para um email sem código ativo";
            log.warn(mensagem + ": {}", emailNormalizado);
            return new AutenticacaoResposta(false, mensagem);
        }

        if (LocalDateTime.now().isAfter(codigoArmazenado.expiracao())) {
            final String mensagem = "Código expirado";
            log.info(mensagem + " para: {}", emailNormalizado);
            removerCodigo(emailNormalizado);
            return new AutenticacaoResposta(false, mensagem);
        }
        if (codigoArmazenado.tentativas() >= MAX_TENTATIVAS) {
            final String mensagem = "Máximo de tentativas excedido";
            log.warn(mensagem + " para: {}", emailNormalizado);
            removerCodigo(emailNormalizado);
            return new AutenticacaoResposta(false, mensagem);
        }
        
        boolean valido = codigo.equals(codigoArmazenado.codigo());
        String mensagem = "";
        if (valido) {
            log.info("Código validado com sucesso para: {}", emailNormalizado);
            removerCodigo(emailNormalizado);
        } else {
            int novasTentativas = codigoArmazenado.tentativas() + 1;
            codigosAtivos.put(emailNormalizado, new CodigoAutenticacao(codigoArmazenado.codigo(), codigoArmazenado.expiracao(), novasTentativas));
            mensagem = "Tentativa inválida " + novasTentativas + " de " + MAX_TENTATIVAS;
            log.warn(mensagem + " para: {}", emailNormalizado);
        }
        return new AutenticacaoResposta(valido, mensagem);
    }

    public void removerCodigo(String email) {
        String emailNormalizado = email.toLowerCase().trim();
        codigosAtivos.remove(emailNormalizado);
        tentativasFalhas.remove(emailNormalizado);
    }
    
    private String gerarCodigoAleatorio() {
        String caracteres = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder codigo = new StringBuilder(CODIGO_TAMANHO);
        for (int i = 0; i < CODIGO_TAMANHO; i++) {
            int indice = random.nextInt(caracteres.length());
            codigo.append(caracteres.charAt(indice));
        }
        return codigo.toString();
    }
    @Scheduled(fixedDelay = 300000) // Executa a cada 5 minutos
    public void limparCodigosExpirados() {
        log.debug("Iniciando limpeza de códigos expirados");
        LocalDateTime agora = LocalDateTime.now();
        int removidos = 0;
        for (Map.Entry<String, CodigoAutenticacao> entry : codigosAtivos.entrySet()) {
            if (agora.isAfter(entry.getValue().expiracao())) {
                codigosAtivos.remove(entry.getKey());
                tentativasFalhas.remove(entry.getKey());
                removidos++;
            }
        }
        
        if (removidos > 0) {
            log.info("Removidos {} códigos expirados", removidos);
        } else {
            log.debug("Nenhum código expirado encontrado");
        }
    }
    private record CodigoAutenticacao(String codigo, LocalDateTime expiracao, int tentativas) {}

    public record AutenticacaoResposta(boolean valido, String mensagem) {}
}
