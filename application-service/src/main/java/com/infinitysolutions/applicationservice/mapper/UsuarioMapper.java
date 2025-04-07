package com.infinitysolutions.applicationservice.mapper;

import com.infinitysolutions.applicationservice.model.PessoaFisica;
import com.infinitysolutions.applicationservice.model.PessoaJuridica;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.dto.*;

public class UsuarioMapper {

    private UsuarioMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Usuario toUsuario(String nome, String telefone) {
        return new Usuario(nome, telefone);
    }

    public static PessoaFisica toPessoaFisica(Usuario usuario, String rg, String cpf) {
        String cpfLimpo = cpf.replaceAll("[.\\-\\s]", "");
        return new PessoaFisica(usuario, rg, cpfLimpo);
    }

    public static PessoaJuridica toPessoaJuridica(Usuario usuario, String telefoneResidencial, String cnpj, String razaoSocial) {
        String cnpjLimpo = cnpj.replaceAll("[.\\-/\\\\s]", "");
        return new PessoaJuridica(usuario, telefoneResidencial, cnpjLimpo, razaoSocial);
    }

    public static PessoaFisicaRespostaCadastroDTO toPessoaFisicaRespostaCadastroDTO(PessoaFisica pessoaFisica) {
        return new PessoaFisicaRespostaCadastroDTO(
                pessoaFisica.getId(),
                pessoaFisica.getUsuario().getNome(),
                pessoaFisica.getUsuario().getTelefoneCelular(),
                new EnderecoResumidoDTO(
                        pessoaFisica.getUsuario().getEndereco().getCep(),
                        pessoaFisica.getUsuario().getEndereco().getLogradouro(),
                        pessoaFisica.getUsuario().getEndereco().getNumero(),
                        pessoaFisica.getUsuario().getEndereco().getCidade(),
                        pessoaFisica.getUsuario().getEndereco().getEstado()),
                pessoaFisica.getCpf());
    }

    public static PessoaJuridicaRespostaCadastroDTO toPessoaJuridicaRespostaCadastroDTO(PessoaJuridica pessoaJuridica) {
        return new PessoaJuridicaRespostaCadastroDTO(
                pessoaJuridica.getId(),
                pessoaJuridica.getUsuario().getNome(),
                pessoaJuridica.getUsuario().getTelefoneCelular(),
                new EnderecoResumidoDTO(
                        pessoaJuridica.getUsuario().getEndereco().getCep(),
                        pessoaJuridica.getUsuario().getEndereco().getLogradouro(),
                        pessoaJuridica.getUsuario().getEndereco().getNumero(),
                        pessoaJuridica.getUsuario().getEndereco().getCidade(),
                        pessoaJuridica.getUsuario().getEndereco().getEstado()),
                pessoaJuridica.getCnpj(),
                pessoaJuridica.getRazaoSocial(),
                pessoaJuridica.getTelefoneResidencial()
        );
    }

    public static PessoaFisicaDTO toPessoaFisicaDTO(PessoaFisica pessoaFisica, boolean possuiCopiaRG, boolean cadastroCompleto) {
        return new PessoaFisicaDTO(
                pessoaFisica.getId(),
                pessoaFisica.getUsuario().getNome(),
                pessoaFisica.getUsuario().getTelefoneCelular(),
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

    public static PessoaJuridicaDTO toPessoaJuridicaDTO(PessoaJuridica pessoaJuridica, boolean possuiCartaoCnpj, boolean possuiContratoSocial, boolean cadastroCompleto) {
        return new PessoaJuridicaDTO(
                pessoaJuridica.getId(),
                pessoaJuridica.getUsuario().getNome(),
                pessoaJuridica.getUsuario().getTelefoneCelular(),
                pessoaJuridica.getTelefoneResidencial(),
                new EnderecoDTO(
                        pessoaJuridica.getUsuario().getEndereco().getCep(),
                        pessoaJuridica.getUsuario().getEndereco().getLogradouro(),
                        pessoaJuridica.getUsuario().getEndereco().getBairro(),
                        pessoaJuridica.getUsuario().getEndereco().getCidade(),
                        pessoaJuridica.getUsuario().getEndereco().getEstado(),
                        pessoaJuridica.getUsuario().getEndereco().getNumero(),
                        pessoaJuridica.getUsuario().getEndereco().getComplemento()
                ),
                pessoaJuridica.getCnpj(),
                pessoaJuridica.getRazaoSocial(),
                possuiContratoSocial,
                possuiCartaoCnpj,
                cadastroCompleto
        );
    }
}
