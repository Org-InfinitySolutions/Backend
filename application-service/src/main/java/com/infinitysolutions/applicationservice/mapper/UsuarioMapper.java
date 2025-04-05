package com.infinitysolutions.applicationservice.mapper;

import com.infinitysolutions.applicationservice.model.PessoaFisica;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.dto.*;

public class UsuarioMapper {
    public static Usuario toUsuario(String nome, String telefone) {
        return new Usuario(nome, telefone);
    }

    public static PessoaFisica toPessoaFisica(Usuario usuario, String rg, String cpf) {
        return new PessoaFisica(usuario, rg, cpf);
    }
    public static PessoaFisicaRespostaCadastroDTO toPessoaFisicaRespostaCadastroDTO(PessoaFisica pessoaFisica) {
        return new PessoaFisicaRespostaCadastroDTO(
                pessoaFisica.getId(),
                pessoaFisica.getUsuario().getNome(),
                pessoaFisica.getUsuario().getTelefone(),
                new EnderecoResumidoDTO(
                        pessoaFisica.getUsuario().getEndereco().getCep(),
                        pessoaFisica.getUsuario().getEndereco().getLogradouro(),
                        pessoaFisica.getUsuario().getEndereco().getNumero(),
                        pessoaFisica.getUsuario().getEndereco().getCidade(),
                        pessoaFisica.getUsuario().getEndereco().getEstado()),
                pessoaFisica.getCpf());
    }

    public static PessoaFisicaDTO toPessoaFisicaDTO(PessoaFisica pessoaFisica, String email, boolean possuiCopiaRG, boolean cadastroCompleto) {
        return new PessoaFisicaDTO(
                pessoaFisica.getId(),
                pessoaFisica.getUsuario().getNome(),
                pessoaFisica.getUsuario().getTelefone(),
                email,
                new EnderecoDTO(
                pessoaFisica.getUsuario().getEndereco().getCep(),
                pessoaFisica.getUsuario().getEndereco().getLogradouro(),
                pessoaFisica.getUsuario().getEndereco().getBairro(),
                pessoaFisica.getUsuario().getEndereco().getCidade(),
                pessoaFisica.getUsuario().getEndereco().getEstado(),
                pessoaFisica.getUsuario().getEndereco().getNumero(),
                pessoaFisica.getUsuario().getEndereco().getComplemento()
                ),
                pessoaFisica.getCpf(),
                pessoaFisica.getRg(),
                possuiCopiaRG,
                cadastroCompleto
        );
    }
}
