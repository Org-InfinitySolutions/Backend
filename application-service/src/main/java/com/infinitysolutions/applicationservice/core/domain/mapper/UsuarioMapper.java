package com.infinitysolutions.applicationservice.core.domain.mapper;

import com.infinitysolutions.applicationservice.core.domain.Endereco;
import com.infinitysolutions.applicationservice.core.domain.PessoaFisica;
import com.infinitysolutions.applicationservice.core.domain.PessoaJuridica;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoUsuario;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.CriarPFInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.CriarPJInput;

public class UsuarioMapper {
    private UsuarioMapper() { throw new IllegalStateException("Utility class");}

    public static PessoaFisica toPessoaFisica(CriarPFInput input, Endereco endereco) {
        return new PessoaFisica(
                null,
                input.nome,
                input.telefoneCelular,
                endereco,
                input.tipo.equals("PF") ? TipoUsuario.PF : TipoUsuario.PJ,
                input.cpf,
                input.rg
        );
    }

    public static PessoaJuridica toPessoaJuridica(CriarPJInput input, Endereco endereco) {
        return new PessoaJuridica(
                null,
                input.nome,
                input.telefoneCelular,
                endereco,
                input.tipo.equals("PF") ? TipoUsuario.PF : TipoUsuario.PJ,
                input.telefoneResidencial,
                input.cnpj,
                input.razaoSocial
        );
    }

}
