package com.infinitysolutions.applicationservice.core.domain.produto;

import com.infinitysolutions.applicationservice.core.domain.ArquivoMetadado;
import com.infinitysolutions.applicationservice.core.exception.ProdutoException;
import com.infinitysolutions.applicationservice.core.usecases.produto.AtualizarProdutoInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Produto {

    private Integer id;
    private String modelo;
    private String marca;
    private String urlFabricante;
    private List<ArquivoMetadado> imagens = new ArrayList<>();
    private String descricao;
    private Integer qtdEstoque;
    private boolean isAtivo;
    private Categoria categoria;

    public Produto(Integer id, String modelo, String marca, String urlFabricante, 
                   List<ArquivoMetadado> imagens, String descricao, Integer qtdEstoque, 
                   boolean isAtivo, Categoria categoria) {
        this.id = id;
        this.setModelo(modelo);
        this.setMarca(marca);
        this.setUrlFabricante(urlFabricante);
        this.imagens = imagens != null ? new ArrayList<>(imagens) : new ArrayList<>();
        this.setDescricao(descricao);
        this.setQtdEstoque(qtdEstoque);
        this.isAtivo = isAtivo;
        this.setCategoria(categoria);
    }

    public Produto(String modelo, String marca, String urlFabricante, String descricao, Integer qtdEstoque, Categoria categoria) {
        this(null, modelo, marca, urlFabricante, new ArrayList<>(), descricao, qtdEstoque, true, categoria);
    }

    public void atualizarDados(AtualizarProdutoInput input, Categoria categoria) {
        if (input.modelo() != null) setModelo(input.modelo());
        if (input.marca() != null) setMarca(input.marca());
        if (input.urlFabricante() != null) setUrlFabricante(input.urlFabricante());
        setAtivo(input.isAtivo());

        if (input.descricao() != null) setDescricao(input.descricao());
        if (input.qtdEstoque() != null) setQtdEstoque(input.qtdEstoque());
        if (categoria != null) setCategoria(categoria);
    }

    public Integer getId() {
        return id;
    }

    public String getModelo() {
        return modelo;
    }

    public String getMarca() {
        return marca;
    }

    public String getUrlFabricante() {
        return urlFabricante;
    }

    public List<ArquivoMetadado> getImagens() {
        return Collections.unmodifiableList(imagens);
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getQtdEstoque() {
        return qtdEstoque;
    }

    public boolean isAtivo() {
        return isAtivo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setModelo(String modelo) {
        validateModelo(modelo);
        this.modelo = modelo.trim();
    }

    public void setMarca(String marca) {
        validateMarca(marca);
        this.marca = marca.trim();
    }

    public void setAtivo(boolean isAtivo) {
        this.isAtivo = isAtivo;
    }

    public void setUrlFabricante(String urlFabricante) {
        if (urlFabricante != null && !urlFabricante.trim().isEmpty()) {
            validateUrl(urlFabricante);
            this.urlFabricante = urlFabricante.trim();
        } else {
            this.urlFabricante = urlFabricante;
        }
    }

    public void setDescricao(String descricao) {
        if (descricao != null) {
            this.descricao = descricao.trim();
        } else {
            this.descricao = descricao;
        }
    }

    public void setQtdEstoque(Integer qtdEstoque) {
        validateQtdEstoque(qtdEstoque);
        this.qtdEstoque = qtdEstoque;
    }

    public void setCategoria(Categoria categoria) {
        validateCategoria(categoria);

        if (this.categoria != null && this.categoria != categoria) {
            this.categoria.removerProduto(this);
        }
        
        this.categoria = categoria;

        if (categoria != null && !categoria.getProdutos().contains(this)) {
            categoria.adicionarProdutoInterno(this);
        }
    }

    public void setCategoriaInterno(Categoria categoria) {
        this.categoria = categoria;
    }

    public void ativar() {
        if (categoria == null || !categoria.isAtivo()) {
            throw new ProdutoException("Não é possível ativar produto sem categoria ou com categoria inativa");
        }
        this.isAtivo = true;
    }

    public void inativar() {
        this.isAtivo = false;
    }

    public void adicionarEstoque(Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new ProdutoException("Quantidade a adicionar deve ser maior que zero");
        }
        this.qtdEstoque += quantidade;
    }

    public void removerEstoque(Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new ProdutoException("Quantidade a remover deve ser maior que zero");
        }
        if (this.qtdEstoque < quantidade) {
            throw new ProdutoException("Estoque insuficiente. Estoque atual: " + this.qtdEstoque);
        }
        this.qtdEstoque -= quantidade;
    }

    public boolean isDisponivel() {
        return isAtivo && qtdEstoque > 0;
    }

    public boolean isEstoqueBaixo(int limiteMinimo) {
        return qtdEstoque <= limiteMinimo;
    }

    public void adicionarImagem(ArquivoMetadado imagem) {
        if (imagem == null) {
            throw new ProdutoException("Imagem não pode ser nula");
        }
        if (!this.imagens.contains(imagem)) {
            this.imagens.add(imagem);
        }
    }

    public void removerImagem(ArquivoMetadado imagem) {
        if (imagem != null) {
            this.imagens.remove(imagem);
        }
    }

    public void limparImagens() {
        this.imagens.clear();
    }

    public int getQuantidadeImagens() {
        return imagens.size();
    }

    private void validateModelo(String modelo) {
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new ProdutoException("Modelo do produto é obrigatório");
        }
        if (modelo.trim().length() > 255) {
            throw new ProdutoException("Modelo do produto não pode exceder 255 caracteres");
        }
    }

    private void validateMarca(String marca) {
        if (marca == null || marca.trim().isEmpty()) {
            throw new ProdutoException("Marca do produto é obrigatória");
        }
        if (marca.trim().length() > 255) {
            throw new ProdutoException("Marca do produto não pode exceder 255 caracteres");
        }
    }

    private void validateUrl(String url) {
        if (url.length() > 500) {
            throw new ProdutoException("URL do fabricante não pode exceder 500 caracteres");
        }
        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("www.")) {
            throw new ProdutoException("URL do fabricante deve ser uma URL válida");
        }
    }

    private void validateQtdEstoque(Integer qtdEstoque) {
        if (qtdEstoque == null || qtdEstoque < 0) {
            throw new ProdutoException("Quantidade em estoque deve ser maior ou igual a zero");
        }
    }

    private void validateCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new ProdutoException("Categoria do produto é obrigatória");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Produto produto = (Produto) obj;
        return Objects.equals(id, produto.id) && 
               Objects.equals(modelo, produto.modelo) && 
               Objects.equals(marca, produto.marca);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, modelo, marca);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", modelo='" + modelo + '\'' +
                ", marca='" + marca + '\'' +
                ", qtdEstoque=" + qtdEstoque +
                ", isAtivo=" + isAtivo +
                ", categoria=" + (categoria != null ? categoria.getNome() : "null") +
                '}';
    }
}
