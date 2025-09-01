package com.infinitysolutions.applicationservice.core.usecases.usuario;

import com.infinitysolutions.applicationservice.core.exception.EstrategiaNaoEncontradaException;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.CriarPFInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.CriarPessoaFisica;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.CriarPJInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.CriarPessoaJuridica;
import com.infinitysolutions.applicationservice.core.domain.Usuario;

public class CriarUsuario {

    private final CriarPessoaJuridica criarPessoaJuridica;
    private final CriarPessoaFisica criarPessoaFisica;

    public CriarUsuario(CriarPessoaJuridica criarPessoaJuridica, CriarPessoaFisica criarPessoaFisica) {
        this.criarPessoaJuridica = criarPessoaJuridica;
        this.criarPessoaFisica = criarPessoaFisica;
    }

    public Usuario execute(CriarUsuarioInput input) {

        Usuario usuarioCadastrado = switch (input.tipo) {
            case "PF" -> criarPessoaFisica.execute((CriarPFInput) input);
            case "PJ" -> criarPessoaJuridica.execute((CriarPJInput) input);
            default -> throw new EstrategiaNaoEncontradaException(input.getClass().toString());
        };



//        AuthServiceCadastroRequestDTO authServiceCadastroRequestDTO = credencialAdapter.adaptarParaCadastroRequest(usuarioCadastrado.getId(), usuarioCadastroDTO);
//        httpAuthServiceConnection.enviarCredenciais(authServiceCadastroRequestDTO);

        // Se chegou até aqui, o cadastro foi bem-sucedido, então podemos enviar os emails

        // Preparar o DTO para os emails
//        UsuarioAutenticacaoCadastroDTO dto = new UsuarioAutenticacaoCadastroDTO(usuarioCadastroDTO.getNome(), usuarioCadastroDTO.getEmail());

        // Enviar email de cadastro completo para o usuário
//        try {
//            envioEmailService.enviarCadastroSucessoUsuario(dto);
//            log.info("Email de cadastro completo enviado com sucesso para o usuário: {}", dto.email());
//        } catch (Exception e) {
//            log.error("Erro ao enviar email de cadastro completo para o usuário: {}", e.getMessage());
            // Não interrompe o fluxo se houver erro no envio
//        }

        // Enviar notificação de novo cadastro para o administrador
//        try {
//            envioEmailService.enviarNotificacaoNovoCadastro(dto);
//            log.info("Email de notificação de novo cadastro enviado para o administrador");
//        } catch (Exception e) {
//            log.error("Erro ao enviar email de notificação de novo cadastro para o administrador: {}", e.getMessage());
            // Não interrompe o fluxo se houver erro no envio
//        }

        return usuarioCadastrado;
    }
}
