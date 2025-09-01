package com.infinitysolutions.applicationservice.core.domain;

import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoAnexo;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoUsuario;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.AtualizarPessoaJuridicaInput;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PessoaJuridica extends Usuario {

    private String telefoneResidencial;
    private String cnpj;
    private String razaoSocial;
    private boolean possuiContratoSocial;
    private boolean possuiCartaoCnpj;


    public PessoaJuridica(UUID id, String nome, String telefoneCelular, Endereco endereco, TipoUsuario tipoUsuario, String telefoneResidencial, String cnpj, String razaoSocial) {
        super(id, nome, telefoneCelular, endereco, tipoUsuario);
        this.telefoneResidencial = telefoneResidencial;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
    }

    public PessoaJuridica(
            UUID id,
            String nome,
            String telefoneCelular,
            Endereco endereco,
            TipoUsuario tipoUsuario,
            LocalDateTime dataCriacao,
            LocalDateTime dataAtualizacao,
            boolean isAtivo,
            String cnpj,
            String razaoSocial,
            String telefoneResidencial,
            List<ArquivoMetadado> documentos
    ) {
        super(id, nome, telefoneCelular, endereco, tipoUsuario);
        this.setDataCriacao(dataCriacao);
        this.setDataAtualizacao(dataAtualizacao);
        this.setAtivo(isAtivo);
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.telefoneResidencial = telefoneResidencial;
        this.setDocumentos(documentos);

    }


    @Override
    public void setCadastroCompleto(List<ArquivoMetadado> documentos) {
        boolean possuiComprovanteEndereco = documentos.stream().anyMatch(d -> d.getTipoAnexo().equals(TipoAnexo.COMPROVANTE_ENDERECO));
        boolean contratoSocial = documentos.stream().anyMatch(d -> d.getTipoAnexo().equals(TipoAnexo.COPIA_CONTRATO_SOCIAL));
        boolean cartaoCnpj = documentos.stream().anyMatch(d -> d.getTipoAnexo().equals(TipoAnexo.COPIA_CNPJ));

        this.setPossuiComprovanteEndereco(possuiComprovanteEndereco);
        this.setPossuiContratoSocial(contratoSocial);
        this.setPossuiCartaoCnpj(cartaoCnpj);
        this.possuiCadastroCompleto = possuiComprovanteEndereco && contratoSocial && cartaoCnpj;
    }

    public void atualizarDados(AtualizarPessoaJuridicaInput input, Endereco endereco) {
        if (input.getNome() != null) setNome(input.getNome());
        if (input.getEndereco() != null) setEndereco(endereco);
        if (input.getTelefoneCelular() != null) setTelefoneCelular(input.getTelefoneCelular());
        if (input.getTelefoneResidencial() != null) setTelefoneResidencial(input.getTelefoneResidencial());
        if (input.getRazaoSocial() != null) setRazaoSocial(input.getRazaoSocial());
    }

    public String getTelefoneResidencial() {
        return telefoneResidencial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public boolean possuiContratoSocial() {
        return possuiContratoSocial;
    }

    public void setTelefoneResidencial(String telefoneResidencial) {
        this.telefoneResidencial = telefoneResidencial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public void setPossuiContratoSocial(boolean possuiContratoSocial) {
        this.possuiContratoSocial = possuiContratoSocial;
    }

    public boolean possuiCartaoCnpj() {
        return possuiCartaoCnpj;
    }

    public void setPossuiCartaoCnpj(boolean possuiCartaoCnpj) {
        this.possuiCartaoCnpj = possuiCartaoCnpj;
    }



}
