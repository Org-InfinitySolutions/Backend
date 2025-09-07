package com.infinitysolutions.applicationservice.core.usecases.credencial.resetsenha;

import com.infinitysolutions.applicationservice.core.domain.usuario.Credencial;
import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import com.infinitysolutions.applicationservice.core.gateway.CodigoAutenticacaoGateway;
import com.infinitysolutions.applicationservice.core.gateway.CredenciaisGateway;
import com.infinitysolutions.applicationservice.core.usecases.credencial.BuscarCredencialPorEmail;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailCodigoResetSenha;
import com.infinitysolutions.applicationservice.core.usecases.usuario.BuscarUsuarioPorId;

public class EnviarCodigoResetSenha {

    private final CredenciaisGateway credenciaisGateway;
    private final CodigoAutenticacaoGateway codigoAutenticacaoGateway;
    private final EnviarEmailCodigoResetSenha enviarEmailCodigoResetSenha;
    private final BuscarCredencialPorEmail buscarCredencialPorEmail;
    private final BuscarUsuarioPorId buscarUsuarioPorId;

    public EnviarCodigoResetSenha(CredenciaisGateway credenciaisGateway,
                                  CodigoAutenticacaoGateway codigoAutenticacaoGateway,
                                  EnviarEmailCodigoResetSenha enviarEmailCodigoResetSenha,
                                  BuscarCredencialPorEmail buscarCredencialPorEmail,
                                  BuscarUsuarioPorId buscarUsuarioPorId) {
        this.credenciaisGateway = credenciaisGateway;
        this.codigoAutenticacaoGateway = codigoAutenticacaoGateway;
        this.enviarEmailCodigoResetSenha = enviarEmailCodigoResetSenha;
        this.buscarCredencialPorEmail = buscarCredencialPorEmail;
        this.buscarUsuarioPorId = buscarUsuarioPorId;
    }

    public RespostaResetSenha execute(String email) {
        try {
            if (!credenciaisGateway.existsByEmail(email)) {
                return new RespostaResetSenha(true, "Se o email estiver cadastrado, você receberá um código para reset de senha");
            }
            Credencial credencialEncontrada = buscarCredencialPorEmail.execute(Email.of(email));
            String codigo = codigoAutenticacaoGateway.gerarCodigo(Email.of(email));
            String nomeUsuario = buscarUsuarioPorId.execute(credencialEncontrada.getUsuarioId()).getNome();
            enviarEmailCodigoResetSenha.execute(Email.of(email), nomeUsuario, codigo);
            return new RespostaResetSenha(true, "Código de reset de senha enviado com sucesso para o email fornecido");

        } catch (Exception e) {
            return new RespostaResetSenha(false, "Erro interno. Tente novamente mais tarde");
        }
    }
}
