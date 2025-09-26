package com.infinitysolutions.applicationservice.core.usecases.credencial.resetsenha;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.gateway.CodigoAutenticacaoGateway;
import com.infinitysolutions.applicationservice.core.usecases.credencial.AlterarSenhaCredencial;

import java.util.Map;

public class ResetarSenha {

    private final CodigoAutenticacaoGateway codigoAutenticacaoGateway;
    private final AlterarSenhaCredencial alterarSenhaCredencial;

    public ResetarSenha(CodigoAutenticacaoGateway codigoAutenticacaoGateway, AlterarSenhaCredencial alterarSenhaCredencial) {
        this.codigoAutenticacaoGateway = codigoAutenticacaoGateway;
        this.alterarSenhaCredencial = alterarSenhaCredencial;
    }

    public RespostaResetSenha execute(String email, String codigo, String novaSenha) {

        try {
            Map.Entry<Boolean, String> validacao = codigoAutenticacaoGateway.validarCodigoAutenticacao(Email.of(email), codigo);

            if (!validacao.getKey()) {
                String mensagem = validacao.getValue();

                if (mensagem.contains("expirado")) {
                    return new RespostaResetSenha(false, "Código expirado. Solicite um novo código");
                } else if (mensagem.contains("excedido")) {
                    return new RespostaResetSenha(false, "Número máximo de tentativas excedido. Solicite um novo código");
                } else {
                    return new RespostaResetSenha(false, "Código inválido");
                }
            }
            alterarSenhaCredencial.execute(email, novaSenha);
            return new RespostaResetSenha(true, "Senha alterada com sucesso");

        } catch (RecursoNaoEncontradoException e) {
            return new RespostaResetSenha(false, "Email não encontrado");
        } catch (Exception e) {
            return new RespostaResetSenha(false, "Erro interno. Tente novamente mais tarde");
        }
    }
}
