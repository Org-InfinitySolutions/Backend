package com.infinitysolutions.applicationservice.mapper;

import com.infinitysolutions.applicationservice.model.PessoaFisica;
import com.infinitysolutions.applicationservice.model.PessoaJuridica;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.dto.endereco.EnderecoDTO;
import com.infinitysolutions.applicationservice.model.dto.endereco.EnderecoResumidoDTO;
import com.infinitysolutions.applicationservice.model.dto.pessoa.fisica.PessoaFisicaDTO;
import com.infinitysolutions.applicationservice.model.dto.pessoa.fisica.PessoaFisicaRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.pessoa.juridica.PessoaJuridicaDTO;
import com.infinitysolutions.applicationservice.model.dto.pessoa.juridica.PessoaJuridicaRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioRespostaDTO;

import java.util.List;

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

    public static PessoaFisicaDTO toPessoaFisicaDTO(PessoaFisica pessoaFisica, boolean possuiCopiaRG, boolean possuiComprovanteEndereco, boolean cadastroCompleto, List<UsuarioRespostaDTO.DocumentoUsuarioDTO> documentosUsuario) {
        return new PessoaFisicaDTO(
                pessoaFisica.getId(),
                pessoaFisica.getUsuario().getNome(),
                null,
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
                pessoaFisica.getUsuario().getDataCriacao(),
                pessoaFisica.getUsuario().getDataAtualizacao(),
                pessoaFisica.getCpf(),
                pessoaFisica.getRg(),
                possuiCopiaRG,
                possuiComprovanteEndereco,
                cadastroCompleto,
                documentosUsuario
        );
    }

    public static PessoaJuridicaDTO toPessoaJuridicaDTO(PessoaJuridica pessoaJuridica, boolean possuiCartaoCnpj, boolean possuiContratoSocial, boolean possuiComprovanteEndereco, boolean cadastroCompleto, List<UsuarioRespostaDTO.DocumentoUsuarioDTO> documentosUsuario) {

        return new PessoaJuridicaDTO(
                pessoaJuridica.getId(),
                pessoaJuridica.getUsuario().getNome(),
                null,
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
                pessoaJuridica.getUsuario().getDataCriacao(),
                pessoaJuridica.getUsuario().getDataAtualizacao(),
                pessoaJuridica.getCnpj(),
                pessoaJuridica.getRazaoSocial(),
                possuiContratoSocial,
                possuiCartaoCnpj,
                possuiComprovanteEndereco,
                cadastroCompleto,
                documentosUsuario
        );
    }
}
