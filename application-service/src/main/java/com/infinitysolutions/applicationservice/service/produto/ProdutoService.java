package com.infinitysolutions.applicationservice.service.produto;


import com.infinitysolutions.applicationservice.infra.exception.EntidadeNaoEncontradaException;
import com.infinitysolutions.applicationservice.infra.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.infra.exception.VinculoExistenteException;
import com.infinitysolutions.applicationservice.mapper.produto.ProdutoMapper;
import com.infinitysolutions.applicationservice.model.dto.produto.ProdutoAtualizacaoDTO;
import com.infinitysolutions.applicationservice.model.dto.produto.ProdutoCriacaoDTO;
import com.infinitysolutions.applicationservice.model.dto.produto.ProdutoRespostaDTO;
import com.infinitysolutions.applicationservice.model.enums.TipoAnexo;
import com.infinitysolutions.applicationservice.model.produto.Categoria;
import com.infinitysolutions.applicationservice.model.produto.Produto;
import com.infinitysolutions.applicationservice.repository.produto.ProdutoRepository;
import com.infinitysolutions.applicationservice.repository.produto.ProdutoPedidoRepository;
import com.infinitysolutions.applicationservice.service.ArquivoMetadadosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProdutoService {

    private final CategoriaService categoriaService;
    private final ProdutoRepository repository;
    private final ProdutoPedidoRepository produtoPedidoRepository;
    private final ArquivoMetadadosService arquivoMetadadosService;
    public List<ProdutoRespostaDTO> listarTodosProdutos() {
        return repository.findAllByIsAtivoTrue()
                .stream()
                .map(ProdutoMapper::toProdutoGenericoRespostaDTO)
                .toList();
    }

    public List<ProdutoRespostaDTO> listarTodosProdutosAdmin() {
        return repository.findAll()
                .stream()
                .map(ProdutoMapper::toProdutoGenericoRespostaDTO)
                .toList();
    }

    private Produto findById(Integer id) {
        log.info("Buscando produto com ID: {}", id);
        Optional<Produto> produto = repository.findByIdAndIsAtivoTrue(id);
        if (produto.isEmpty()) {
            log.warn("Produto com o ID: {} não encontrado", id);
            throw RecursoNaoEncontradoException.produtoNaoEncontrado(id);
        }
        return produto.get();
    }

    private Produto findByIdAdmin(Integer id) {
        log.info("Buscando produto com ID (admin): {}", id);
        Optional<Produto> produto = repository.findById(id);
        if (produto.isEmpty()) {
            log.warn("Produto com o ID: {} não encontrado", id);
            throw RecursoNaoEncontradoException.produtoNaoEncontrado(id);
        }
        return produto.get();
    }

    public ProdutoRespostaDTO buscarPorId(Integer id) {
        return ProdutoMapper.toProdutoGenericoRespostaDTO(findById(id));
    }

    public ProdutoRespostaDTO buscarPorIdAdmin(Integer id) {
        return ProdutoMapper.toProdutoGenericoRespostaDTO(findByIdAdmin(id));
    }
    
    @Transactional
    public ProdutoRespostaDTO criar(ProdutoCriacaoDTO dto, MultipartFile imagem) {
        Categoria categoria = categoriaService.findById(dto.getCategoriaId());

        Produto produto = ProdutoMapper.toProduto(dto, categoria);
        if (imagem != null && !imagem.isEmpty()) {
            arquivoMetadadosService.uploadAndPersistArquivo(imagem, TipoAnexo.IMAGEM_PRODUTO, produto);
        }

        Produto produtoSalvo = repository.save(produto);
        return ProdutoMapper.toProdutoGenericoRespostaDTO(produtoSalvo);
    }
    
    @Transactional
    public ProdutoRespostaDTO atualizar(Integer id, ProdutoAtualizacaoDTO dto) {
        Produto produto = findByIdAdmin(id);

        Categoria categoria = categoriaService.findById(dto.getCategoriaId());

        ProdutoMapper.atualizarProduto(produto, dto, categoria);
        Produto produtoAtualizado = repository.save(produto);
        return ProdutoMapper.toProdutoGenericoRespostaDTO(produtoAtualizado);
    }
    
    @Transactional
    public void atualizarImagem(Integer id, MultipartFile novaImagem) {
        log.info("Atualizando imagem do produto ID: {}", id);
        Produto produto = findByIdAdmin(id);
        
        if (novaImagem == null || novaImagem.isEmpty()) {
            throw new IllegalArgumentException("Arquivo de imagem é obrigatório");
        }
        
        arquivoMetadadosService.atualizarImagemProduto(novaImagem, produto);
        log.info("Imagem do produto ID: {} atualizada com sucesso", id);
    }
      @Transactional
    public void remover(Integer id) {
        log.info("Iniciando processo de remoção do produto com ID: {}", id);
        
        if (!repository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Produto não encontrado com o id: " + id);
        }
        
        // Verificar se o produto está vinculado a algum pedido
        if (produtoPedidoRepository.existsByProdutoId(id)) {
            log.warn("Tentativa de exclusão do produto {} que está vinculado a pedidos", id);
            throw VinculoExistenteException.produtoVinculadoAPedidos(id);
        }
        
        log.info("Produto com ID {} não possui vínculos, procedendo com a exclusão", id);
        repository.deleteById(id);
        log.info("Produto com ID {} removido com sucesso", id);
    }
}
