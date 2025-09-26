package com.infinitysolutions.applicationservice.core.domain.pedido;

import com.infinitysolutions.applicationservice.core.domain.ArquivoMetadado;
import com.infinitysolutions.applicationservice.core.domain.Endereco;
import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;
import com.infinitysolutions.applicationservice.core.domain.valueobject.SituacaoPedido;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoPedido;
import com.infinitysolutions.applicationservice.core.usecases.pedido.CadastrarPedidoInput;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private Integer id;

    private Usuario usuario;

    private List<ProdutoPedido> produtosPedido = new ArrayList<>();

    private Endereco endereco;

    private SituacaoPedido situacao = SituacaoPedido.EM_ANALISE;

    private TipoPedido tipo;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataAtualizacao;

    private LocalDateTime dataAprovacao;

    private LocalDateTime dataInicioEvento;

    private LocalDateTime dataFinalizacao;

    private LocalDateTime dataCancelamento;

    private LocalDateTime dataEntrega;

    private LocalDateTime dataRetirada;

    private String descricao;

    private List<ArquivoMetadado> documentos = new ArrayList<>();

    public Pedido(CadastrarPedidoInput input, Usuario usuario, Endereco endereco) {
        this.tipo = input.tipo();
        this.dataEntrega = input.dataEntrega();
        this.dataRetirada = input.dataRetirada();
        this.dataCriacao = LocalDateTime.now();
        this.descricao = input.descricao();
        this.usuario = usuario;
        this.endereco = endereco;
    }

    public Pedido(Integer id, Usuario usuario, List<ProdutoPedido> produtosPedido, Endereco endereco, SituacaoPedido situacao, TipoPedido tipo, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, LocalDateTime dataAprovacao, LocalDateTime dataInicioEvento, LocalDateTime dataFinalizacao, LocalDateTime dataCancelamento, LocalDateTime dataEntrega, LocalDateTime dataRetirada, String descricao, List<ArquivoMetadado> documentos) {
        this.id = id;
        this.usuario = usuario;
        this.produtosPedido = produtosPedido;
        this.endereco = endereco;
        this.situacao = situacao;
        this.tipo = tipo;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.dataAprovacao = dataAprovacao;
        this.dataInicioEvento = dataInicioEvento;
        this.dataFinalizacao = dataFinalizacao;
        this.dataCancelamento = dataCancelamento;
        this.dataEntrega = dataEntrega;
        this.dataRetirada = dataRetirada;
        this.descricao = descricao;
        this.documentos = documentos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<ProdutoPedido> getProdutosPedido() {
        return produtosPedido;
    }

    public void setProdutosPedido(List<ProdutoPedido> produtosPedido) {
        this.produtosPedido = produtosPedido;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public SituacaoPedido getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoPedido situacao) {
        this.situacao = situacao;
    }

    public TipoPedido getTipo() {
        return tipo;
    }

    public void setTipo(TipoPedido tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public LocalDateTime getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(LocalDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public LocalDateTime getDataInicioEvento() {
        return dataInicioEvento;
    }

    public void setDataInicioEvento(LocalDateTime dataInicioEvento) {
        this.dataInicioEvento = dataInicioEvento;
    }

    public LocalDateTime getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(LocalDateTime dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    public LocalDateTime getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(LocalDateTime dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    public LocalDateTime getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public LocalDateTime getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(LocalDateTime dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<ArquivoMetadado> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<ArquivoMetadado> documentos) {
        this.documentos = documentos;
    }

    public Integer getQtdItens() {
        if (this.produtosPedido == null || this.produtosPedido.isEmpty()) return 0;

        return this.produtosPedido.stream().mapToInt(ProdutoPedido::getQtd).sum();
    }
}
