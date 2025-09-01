package com.infinitysolutions.applicationservice.core.domain;

import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoUsuario;
import com.infinitysolutions.applicationservice.core.usecases.usuario.AtualizarUsuarioInput;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Usuario {

    private UUID id;
    private String nome;
    private String telefoneCelular;
    private Endereco endereco;
    private boolean isAtivo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private TipoUsuario tipoUsuario;
    private boolean possuiComprovanteEndereco;
    protected boolean possuiCadastroCompleto;
    private List<ArquivoMetadado> documentos;


    protected Usuario(UUID id, String nome, String telefoneCelular, Endereco endereco, TipoUsuario tipoUsuario) {
        this.id = id;
        this.nome = nome;
        this.telefoneCelular = telefoneCelular;
        this.endereco = endereco;
        this.isAtivo = true;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = null;
        this.tipoUsuario = tipoUsuario;
        this.documentos = new ArrayList<>();
    }

    public abstract void setCadastroCompleto (List<ArquivoMetadado> documentos);

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public void setAtivo(boolean ativo) {
        isAtivo = ativo;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public boolean isAtivo() {
        return isAtivo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public boolean temDocumentos(){
        return this.documentos.isEmpty();
    }

    public List<ArquivoMetadado> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<ArquivoMetadado> documentos) {
        this.documentos = documentos;
    }

    public boolean possuiComprovanteEndereco() {
        return possuiComprovanteEndereco;
    }

    protected void setPossuiComprovanteEndereco(boolean possuiComprovanteEndereco) {
        this.possuiComprovanteEndereco = possuiComprovanteEndereco;
    }

    public boolean getPossuiCadastroCompleto() {
        return possuiCadastroCompleto;
    }

    protected void setPossuiCadastroCompleto(boolean possuiCadastroCompleto) {
        this.possuiCadastroCompleto = possuiCadastroCompleto;
    }
}

