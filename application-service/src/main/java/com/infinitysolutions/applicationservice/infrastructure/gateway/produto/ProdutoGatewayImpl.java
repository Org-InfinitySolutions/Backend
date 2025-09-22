package com.infinitysolutions.applicationservice.infrastructure.gateway.produto;

import com.infinitysolutions.applicationservice.core.domain.produto.Produto;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.gateway.ProdutoGateway;
import com.infinitysolutions.applicationservice.infrastructure.mapper.produto.ProdutoEntityMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.CategoriaEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.ProdutoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.produto.CategoriaRepository;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.produto.ProdutoPedidoRepository;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.produto.ProdutoRepository;
import com.infinitysolutions.applicationservice.old.infra.exception.EntidadeNaoEncontradaException;
import com.infinitysolutions.applicationservice.old.infra.exception.VinculoExistenteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class ProdutoGatewayImpl implements ProdutoGateway {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoPedidoRepository produtoPedidoRepository;

    @Override
    public Produto save(Produto novoProduto) {
        ProdutoEntity produtoEntity = ProdutoEntityMapper.toProdutoEntity(novoProduto);
        return ProdutoEntityMapper.toDomain(produtoRepository.save(produtoEntity));
    }

    @Override
    public List<Produto> findAll(boolean isAdmin) {
        if (isAdmin) {
            return produtoRepository.findAll()
                    .stream().map(ProdutoEntityMapper::toDomain).toList();
        }

        return produtoRepository.findAllByIsAtivoTrue()
                .stream().map(ProdutoEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<Produto> findById(boolean isAdmin, Integer id) {
        if (isAdmin) {
            log.info("Buscando produto com ID (admin): {}", id);
            return produtoRepository.findById(id).map(ProdutoEntityMapper::toDomain);
        }
        log.info("Buscando produto com ID: {}", id);
        return produtoRepository.findByIdAndIsAtivoTrue(id).map(ProdutoEntityMapper::toDomain);
    }

    @Override
    public Produto update(Produto produtoEncontrado) {
        ProdutoEntity entity = produtoRepository.findById(produtoEncontrado.getId()).orElseThrow(() -> RecursoNaoEncontradoException.produtoNaoEncontrado(produtoEncontrado.getId()));
        CategoriaEntity categoriaEntity = categoriaRepository.findByIdAndIsAtivoTrue(produtoEncontrado.getCategoria().getId()).orElseThrow(() -> RecursoNaoEncontradoException.produtoNaoEncontrado(produtoEncontrado.getId()));
        ProdutoEntityMapper.atualizarProduto(entity, produtoEncontrado, categoriaEntity);
        return ProdutoEntityMapper.toDomain(produtoRepository.save(entity));
    }

    @Override
    public void remove(Integer produtoId) {
        log.info("Iniciando processo de remoção do produto com ID: {}", produtoId);

        if (!produtoRepository.existsById(produtoId)) {
            throw new EntidadeNaoEncontradaException("Produto não encontrado com o id: " + produtoId);
        }

        // Verificar se o produto está vinculado a algum pedido
        if (produtoPedidoRepository.existsByProdutoEntityId(produtoId)) {
            log.warn("Tentativa de exclusão do produto {} que está vinculado a pedidos", produtoId);
            throw VinculoExistenteException.produtoVinculadoAPedidos(produtoId);
        }

        log.info("Produto com ID {} não possui vínculos, procedendo com a exclusão", produtoId);
        produtoRepository.deleteById(produtoId);
        log.info("Produto com ID {} removido com sucesso", produtoId);
    }

    @Override
    public Set<Produto> findAllByIds(Set<Integer> ids) {
        return produtoRepository.findByIdInAndIsAtivoTrue(ids).stream().map(ProdutoEntityMapper::toDomain).collect(Collectors.toSet());
    }
}
