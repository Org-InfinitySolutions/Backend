package com.infinitysolutions.applicationservice.core.usecases.usuario;

import com.infinitysolutions.applicationservice.core.exception.EstrategiaNaoEncontradaException;
import com.infinitysolutions.applicationservice.core.usecases.credencial.CriarCredenciais;
import com.infinitysolutions.applicationservice.core.usecases.credencial.CriarCredenciaisInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.CriarPFInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.CriarPessoaFisica;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.CriarPJInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.CriarPessoaJuridica;
import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;

public class CriarUsuario {

    private final CriarPessoaJuridica criarPessoaJuridica;
    private final CriarPessoaFisica criarPessoaFisica;
    private final CriarCredenciais criarCredenciais;

    public CriarUsuario(CriarPessoaJuridica criarPessoaJuridica, CriarPessoaFisica criarPessoaFisica, CriarCredenciais criarCredenciais) {
        this.criarPessoaJuridica = criarPessoaJuridica;
        this.criarPessoaFisica = criarPessoaFisica;
        this.criarCredenciais = criarCredenciais;
    }

    public Usuario execute(CriarUsuarioInput input) {

        Usuario usuarioCadastrado = switch (input.tipo) {
            case "PF" -> criarPessoaFisica.execute((CriarPFInput) input);
            case "PJ" -> criarPessoaJuridica.execute((CriarPJInput) input);
            default -> throw new EstrategiaNaoEncontradaException(input.getClass().toString());
        };

        criarCredenciais.execute(CriarCredenciaisInput.of(input.email, input.senha, usuarioCadastrado.getId(), usuarioCadastrado.getTipoUsuario()));


//        UsuarioAutenticacaoCadastroDTO dto = new UsuarioAutenticacaoCadastroDTO(usuarioCadastroDTO.getNome(), usuarioCadastroDTO.getEmail());

//        try {
//            envioEmailService.enviarCadastroSucessoUsuario(dto);
//            log.info("Email de cadastro completo enviado com sucesso para o usuário: {}", dto.email());
//        } catch (Exception e) {
//            log.error("Erro ao enviar email de cadastro completo para o usuário: {}", e.getMessage());
            // Não interrompe o fluxo se houver erro no envio
//        }

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
