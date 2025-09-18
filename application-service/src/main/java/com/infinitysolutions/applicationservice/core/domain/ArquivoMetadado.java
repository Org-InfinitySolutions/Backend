package com.infinitysolutions.applicationservice.core.domain;

import com.infinitysolutions.applicationservice.core.domain.produto.Produto;
import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoAnexo;
import com.infinitysolutions.applicationservice.core.exception.ArquivoMetadadoException;

import java.time.LocalDateTime;

public class ArquivoMetadado {

    private final Long id;
    private final String originalFilename;
    private final TipoAnexo tipoAnexo;
    private final String mimeType;
    private final Long fileSize;
    private final LocalDateTime uploadedAt;

    private Produto produto;
    private Usuario usuario;
    private Pedido pedido;


    private final String blobName;
    private final String blobUrl;

    public ArquivoMetadado(Long id, String blobName, String blobUrl, String originalFilename, String mimeType, Long fileSize, TipoAnexo tipoAnexo) {
        this.id = id;
        this.blobName = blobName;
        this.blobUrl = blobUrl;
        this.originalFilename = originalFilename;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.uploadedAt = LocalDateTime.now();
        this.tipoAnexo = tipoAnexo;
    }

    public Long getId() {
        return id;
    }

    public String getBlobName() {
        return blobName;
    }

    public String getBlobUrl() {
        return blobUrl;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setProduto(Produto produto) {
        if (this.pedido != null) throw ArquivoMetadadoException.ErroAoSetarAtributo("Erro ao setar o produto, arquivometadado já possui um pedido vinculado.");
        if (this.usuario != null) throw ArquivoMetadadoException.ErroAoSetarAtributo("Erro ao setar o produto, arquivometadado já possui um usuario vinculado.");
        this.produto = produto;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setUsuario(Usuario usuario) {
        if (this.pedido != null) throw ArquivoMetadadoException.ErroAoSetarAtributo("Erro ao setar o usuario, arquivometadado já possui um pedido vinculado.");
        if (this.produto != null) throw ArquivoMetadadoException.ErroAoSetarAtributo("Erro ao setar o usuario, arquivometadado já possui um produto vinculado.");
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setPedido(Pedido pedido) {
        if (this.produto != null) throw ArquivoMetadadoException.ErroAoSetarAtributo("Erro ao setar o pedido, arquivometadado já possui um produto vinculado.");
        if (this.usuario != null) throw ArquivoMetadadoException.ErroAoSetarAtributo("Erro ao setar o pedido, arquivometadado já possui um usuario vinculado.");
        this.pedido = pedido;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public TipoAnexo getTipoAnexo() {
        return tipoAnexo;
    }
}
