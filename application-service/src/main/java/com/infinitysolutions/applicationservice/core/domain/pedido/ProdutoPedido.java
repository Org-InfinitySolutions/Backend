package com.infinitysolutions.applicationservice.core.domain.pedido;

import com.infinitysolutions.applicationservice.core.domain.produto.Produto;

public class ProdutoPedido {

    private Integer id;

    private Produto produto;

    private Pedido pedido;

    private Integer qtd;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }
}
