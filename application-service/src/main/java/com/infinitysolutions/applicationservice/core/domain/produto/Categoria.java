package com.infinitysolutions.applicationservice.core.domain.produto;

import com.infinitysolutions.applicationservice.core.exception.CategoriaException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Categoria {

    private Integer id;
    private String nome;
    private boolean isAtivo;
    private final List<Produto> produtos;

    public Categoria(Integer id, String nome, boolean isAtivo, List<Produto> produtos) {
        this.id = id;
        this.setNome(nome);
        this.isAtivo = isAtivo;
        this.produtos = produtos != null ? new ArrayList<>(produtos) : new ArrayList<>();
    }

    public Categoria(String nome) {
        this(null, nome, true, new ArrayList<>());
    }

    public Categoria(Integer id, String nome) {
        this(id, nome, true, new ArrayList<>());
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public boolean isAtivo() {
        return isAtivo;
    }

    public List<Produto> getProdutos() {
        return Collections.unmodifiableList(produtos);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        validateNome(nome);
        this.nome = nome.trim();
    }

    public void ativar() {
        this.isAtivo = true;
    }

    public void desativar() {
//        if (hasActiveProdutos()) {
//            throw new CategoriaException("Não é possível desativar categoria com produtos ativos");
//        }
        this.isAtivo = false;
    }

    // Métodos de negócio
    public void adicionarProduto(Produto produto) {
        if (produto == null) {
            throw new CategoriaException("Produto não pode ser nulo");
        }
        if (!this.isAtivo) {
            throw new CategoriaException("Não é possível adicionar produto a categoria inativa");
        }
        if (!this.produtos.contains(produto)) {
            this.produtos.add(produto);
            // Manter consistência bidirecional (evitar recursão infinita)
            if (produto.getCategoria() != this) {
                produto.setCategoriaInterno(this);
            }
        }
    }

    public void adicionarProdutoInterno(Produto produto) {
        if (produto != null && !this.produtos.contains(produto)) {
            this.produtos.add(produto);
        }
    }

    public void removerProduto(Produto produto) {
        if (produto != null && this.produtos.contains(produto)) {
            this.produtos.remove(produto);
        }
    }

    public boolean hasActiveProdutos() {
        return produtos.stream().anyMatch(Produto::isAtivo);
    }

    public int getQuantidadeProdutos() {
        return produtos.size();
    }

    public int getQuantidadeProdutosAtivos() {
        return (int) produtos.stream().filter(Produto::isAtivo).count();
    }

    private void validateNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new CategoriaException("Nome da categoria é obrigatório");
        }
        if (nome.trim().length() > 255) {
            throw new CategoriaException("Nome da categoria não pode exceder 255 caracteres");
        }
        if (nome.trim().length() < 2) {
            throw new CategoriaException("Nome da categoria deve ter pelo menos 2 caracteres");
        }
    }

    // Equals e HashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Categoria categoria = (Categoria) obj;
        return Objects.equals(id, categoria.id) && 
               Objects.equals(nome, categoria.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", isAtivo=" + isAtivo +
                ", quantidadeProdutos=" + produtos.size() +
                '}';
    }
}
