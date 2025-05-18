package com.infinitysolutions.applicationservice.mapper.produto;

import com.infinitysolutions.applicationservice.model.dto.produto.ProdutoCriacaoDTO;
import com.infinitysolutions.applicationservice.model.dto.produto.ProdutoRespostaDTO;
import com.infinitysolutions.applicationservice.model.produto.Produto;

public class ProdutoMapper {
    
    public static ProdutoRespostaDTO toProdutoGenericoRespostaDTO(Produto produto) {
        return new ProdutoRespostaDTO(
                produto.getId(), 
                produto.getModelo(), 
                produto.getMarca(),
                produto.getUrlFrabricante(),
                produto.getImagem(),
                produto.getDescricao(),
                produto.getQtdEstoque()
        );
    }

    public static Produto toProduto(ProdutoCriacaoDTO dto) {
        Produto produto = new Produto();
        produto.setModelo(dto.getModelo());
        produto.setMarca(dto.getMarca());
        produto.setUrlFrabricante(dto.getUrlFrabricante());
        produto.setImagem(dto.getImagem());
        produto.setDescricao(dto.getDescricao());
        produto.setQtdEstoque(dto.getQtdEstoque());
        return produto;
    }
    
    public static void atualizarProduto(Produto produto, ProdutoCriacaoDTO dto) {
        produto.setModelo(dto.getModelo());
        produto.setMarca(dto.getMarca());
        produto.setUrlFrabricante(dto.getUrlFrabricante());
        
        if (dto.getImagem() != null) {
            produto.setImagem(dto.getImagem());
        }
        
        produto.setDescricao(dto.getDescricao());
        produto.setQtdEstoque(dto.getQtdEstoque());
    }
}
