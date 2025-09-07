package com.infinitysolutions.applicationservice.core.usecases.usuario;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import com.infinitysolutions.applicationservice.core.exception.EstrategiaNaoEncontradaException;
import com.infinitysolutions.applicationservice.core.usecases.credencial.CriarCredencial;
import com.infinitysolutions.applicationservice.core.usecases.credencial.CriarCredencialInput;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailCadastro;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EnviarEmailInput;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailNotificacaoCadastro;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.CriarPFInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.CriarPessoaFisica;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.CriarPJInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.CriarPessoaJuridica;
import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;

public class CriarUsuario {

    private final CriarPessoaJuridica criarPessoaJuridica;
    private final CriarPessoaFisica criarPessoaFisica;
    private final CriarCredencial criarCredencial;
    private final EnviarEmailCadastro enviarEmailCadastro;
    private final EnviarEmailNotificacaoCadastro enviarEmailNotificacaoCadastro;

    public CriarUsuario(CriarPessoaJuridica criarPessoaJuridica, CriarPessoaFisica criarPessoaFisica, CriarCredencial criarCredencial, EnviarEmailCadastro enviarEmailCadastro, EnviarEmailNotificacaoCadastro enviarEmailNotificacaoCadastro) {
        this.criarPessoaJuridica = criarPessoaJuridica;
        this.criarPessoaFisica = criarPessoaFisica;
        this.criarCredencial = criarCredencial;
        this.enviarEmailCadastro = enviarEmailCadastro;
        this.enviarEmailNotificacaoCadastro = enviarEmailNotificacaoCadastro;
    }

    public Usuario execute(CriarUsuarioInput input) {

        Usuario usuarioCadastrado = switch (input.tipo) {
            case "PF" -> criarPessoaFisica.execute((CriarPFInput) input);
            case "PJ" -> criarPessoaJuridica.execute((CriarPJInput) input);
            default -> throw new EstrategiaNaoEncontradaException(input.getClass().toString());
        };

        criarCredencial.execute(CriarCredencialInput.of(input.email, input.senha, usuarioCadastrado.getId(), usuarioCadastrado.getTipoUsuario()));

        EnviarEmailInput emailInput = new EnviarEmailInput(usuarioCadastrado.getNome(), Email.of(input.email));

        enviarEmailCadastro.execute(emailInput);
        enviarEmailNotificacaoCadastro.execute(emailInput);

        return usuarioCadastrado;
    }
}
