package com.infinitysolutions.applicationservice.infrastructure.gateway.pedido;

import com.infinitysolutions.applicationservice.core.domain.ArquivoMetadado;
import com.infinitysolutions.applicationservice.core.domain.Endereco;
import com.infinitysolutions.applicationservice.core.domain.pedido.Pedido;
import com.infinitysolutions.applicationservice.core.domain.pedido.ProdutoPedido;
import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.gateway.PedidoGateway;
import com.infinitysolutions.applicationservice.core.valueobject.PageResult;
import com.infinitysolutions.applicationservice.infrastructure.mapper.ArquivoMetadadosMapper;
import com.infinitysolutions.applicationservice.infrastructure.mapper.EnderecoMapper;
import com.infinitysolutions.applicationservice.infrastructure.mapper.PedidoMapper;
import com.infinitysolutions.applicationservice.infrastructure.mapper.UsuarioEntityMapper;
import com.infinitysolutions.applicationservice.infrastructure.mapper.produto.ProdutoEntityMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.EnderecoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.PedidoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.UsuarioEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.ProdutoPedidoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.EnderecoRepository;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.PedidoRepository;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.UsuarioRepository;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.produto.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoGatewayImpl implements PedidoGateway {

    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioEntityMapper usuarioEntityMapper;

    @Override
    public Pedido save(Pedido novoPedido) {

        UsuarioEntity usuarioEntity = usuarioRepository.findByIdAndIsAtivoTrue(novoPedido.getUsuario().getId()).orElseThrow(() -> RecursoNaoEncontradoException.usuarioNaoEncontrado(novoPedido.getUsuario().getId()));
        EnderecoEntity enderecoEntity = enderecoRepository.findById(novoPedido.getEndereco().getId()).orElseThrow(() -> RecursoNaoEncontradoException.enderecoNaoEncontrado(novoPedido.getEndereco().getId()));

        PedidoEntity pedidoEntity;
        if (novoPedido.getId() != null) {
            // Fetch the existing entity
            pedidoEntity = pedidoRepository.findById(novoPedido.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado com ID: " + novoPedido.getId()));

            // Update only the fields that should be updated, preserving the existing products
            pedidoEntity.setSituacao(novoPedido.getSituacao());
            pedidoEntity.setDataAtualizacao(novoPedido.getDataAtualizacao());
            pedidoEntity.setDataAprovacao(novoPedido.getDataAprovacao());
            pedidoEntity.setDataInicioEvento(novoPedido.getDataInicioEvento());
            pedidoEntity.setDataFinalizacao(novoPedido.getDataFinalizacao());
            pedidoEntity.setDataCancelamento(novoPedido.getDataCancelamento());
            pedidoEntity.setDataEntrega(novoPedido.getDataEntrega());
            pedidoEntity.setDataRetirada(novoPedido.getDataRetirada());
            pedidoEntity.setDescricao(novoPedido.getDescricao());

            // When updating status, we should keep the existing products
            // Only update products if they are explicitly provided and not empty
            if (novoPedido.getProdutosPedido() != null && !novoPedido.getProdutosPedido().isEmpty()) {
                // We're intentionally NOT updating the products when just updating status
                // This prevents duplicate produto_pedido entries
                log.debug("Products provided during status update, but they will be preserved as is");
            }
        } else {
            // Create a new entity for a new pedido
            pedidoEntity = PedidoMapper.toEntity(
                    usuarioEntity, new ArrayList<>(), enderecoEntity, novoPedido.getSituacao(), novoPedido.getTipo(), novoPedido.getDataCriacao(),
                    novoPedido.getDataAtualizacao(), novoPedido.getDataAprovacao(), novoPedido.getDataInicioEvento(), novoPedido.getDataFinalizacao(),
                    novoPedido.getDataCancelamento(), novoPedido.getDataEntrega(), novoPedido.getDataRetirada(), novoPedido.getDescricao(), new ArrayList<>()
            );

            pedidoEntity = pedidoRepository.save(pedidoEntity);

            // For new pedidos, always set the products
            List<ProdutoPedidoEntity> produtoPedidoEntityList = new ArrayList<>();
            for (ProdutoPedido produtoPedido : novoPedido.getProdutosPedido()) {
                ProdutoPedidoEntity produtoPedidoEntity = new ProdutoPedidoEntity();
                produtoPedidoEntity.setPedidoEntity(pedidoEntity);
                produtoPedidoEntity.setProdutoEntity(produtoRepository.getReferenceById(produtoPedido.getProduto().getId()));
                produtoPedidoEntity.setQtd(produtoPedido.getQtd());
                produtoPedidoEntityList.add(produtoPedidoEntity);
            }
            pedidoEntity.setProdutosPedido(produtoPedidoEntityList);
        }

        PedidoEntity savedEntity = pedidoRepository.save(pedidoEntity);

        // For consistency, we need to fetch the existing product relationships
        List<ProdutoPedido> produtoPedidos = novoPedido.getProdutosPedido();
        if (novoPedido.getId() != null) {
            // For existing pedidos, we should get the products from the database
            produtoPedidos = savedEntity.getProdutosPedido().stream().map(entity -> {
                ProdutoPedido produtoPedido = new ProdutoPedido();
                produtoPedido.setPedido(novoPedido);
                produtoPedido.setProduto(ProdutoEntityMapper.toDomain(entity.getProdutoEntity()));
                produtoPedido.setQtd(entity.getQtd());
                return produtoPedido;
            }).toList();
        }

        return PedidoMapper.toPedido(savedEntity, novoPedido.getUsuario(), produtoPedidos, novoPedido.getEndereco(), novoPedido.getDocumentos());
    }

    @Override
    public Pedido atualizarStatus(Pedido pedido) {
        log.info("Atualizando status do pedido ID: {} para: {}", pedido.getId(), pedido.getSituacao());
        
        PedidoEntity pedidoEntity = pedidoRepository.findById(pedido.getId())
            .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado com ID: " + pedido.getId()));
        
        pedidoEntity.setSituacao(pedido.getSituacao());
        pedidoEntity.setDataAtualizacao(pedido.getDataAtualizacao());
        pedidoEntity.setDataAprovacao(pedido.getDataAprovacao());
        pedidoEntity.setDataInicioEvento(pedido.getDataInicioEvento());
        pedidoEntity.setDataFinalizacao(pedido.getDataFinalizacao());
        pedidoEntity.setDataCancelamento(pedido.getDataCancelamento());
        pedidoEntity.setDataEntrega(pedido.getDataEntrega());
        pedidoEntity.setDataRetirada(pedido.getDataRetirada());
        
        PedidoEntity savedEntity = pedidoRepository.save(pedidoEntity);
        log.info("Status do pedido {} atualizado com sucesso para {}", pedido.getId(), pedido.getSituacao());
        
        Usuario usuario = usuarioEntityMapper.toDomain(savedEntity.getUsuarioEntity());
        Endereco endereco = EnderecoMapper.toDomain(savedEntity.getEnderecoEntity());
        List<ArquivoMetadado> documentos = savedEntity.getDocumentos().stream()
            .map(ArquivoMetadadosMapper::toDomain)
            .toList();
        
        Pedido pedidoAtualizado = PedidoMapper.toPedido(savedEntity, usuario, new ArrayList<>(), endereco, documentos);
        
        List<ProdutoPedido> produtoPedidos = savedEntity.getProdutosPedido().stream().map(entity -> {
            ProdutoPedido produtoPedido = new ProdutoPedido();
            produtoPedido.setPedido(pedidoAtualizado);
            produtoPedido.setProduto(ProdutoEntityMapper.toDomain(entity.getProdutoEntity()));
            produtoPedido.setQtd(entity.getQtd());
            return produtoPedido;
        }).toList();
        
        pedidoAtualizado.setProdutosPedido(produtoPedidos);
        return pedidoAtualizado;
    }

    @Override
    public Optional<Pedido> findById(Integer pedidoId, UUID idUsuario, boolean isAdmin) {
        if (isAdmin) {
            return pedidoRepository.findWithUsuarioEntityById(pedidoId).map(pedidoEntity -> {
                Usuario usuarioProduto = usuarioEntityMapper.toDomain(pedidoEntity.getUsuarioEntity());
                Endereco endereco = EnderecoMapper.toDomain(pedidoEntity.getEnderecoEntity());
                List<ArquivoMetadado> documentos = pedidoEntity.getDocumentos().stream().map(ArquivoMetadadosMapper::toDomain).toList();
                Pedido pedido = PedidoMapper.toPedido(pedidoEntity, usuarioProduto, new ArrayList<>(), endereco, documentos);
                List<ProdutoPedido> produtoPedidos = pedidoEntity.getProdutosPedido().stream().map(entity -> {
                    ProdutoPedido produtoPedido = new ProdutoPedido();
                    produtoPedido.setPedido(pedido);
                    produtoPedido.setProduto(ProdutoEntityMapper.toDomain(entity.getProdutoEntity()));
                    produtoPedido.setQtd(entity.getQtd());
                    return produtoPedido;
                }).toList();
                pedido.setProdutosPedido(produtoPedidos);
                return pedido;
            });
        }

        return pedidoRepository.findWithUsuarioEntityByIdAndByUsuarioEntityId(pedidoId, idUsuario).map(pedidoEntity -> {
            Usuario usuarioProduto = usuarioEntityMapper.toDomain(pedidoEntity.getUsuarioEntity());
            Endereco endereco = EnderecoMapper.toDomain(pedidoEntity.getEnderecoEntity());
            List<ArquivoMetadado> documentos = pedidoEntity.getDocumentos().stream().map(ArquivoMetadadosMapper::toDomain).toList();
            Pedido pedido = PedidoMapper.toPedido(pedidoEntity, usuarioProduto, new ArrayList<>(), endereco, documentos);
            List<ProdutoPedido> produtoPedidos = pedidoEntity.getProdutosPedido().stream().map(entity -> {
                ProdutoPedido produtoPedido = new ProdutoPedido();
                produtoPedido.setPedido(pedido);
                produtoPedido.setProduto(ProdutoEntityMapper.toDomain(entity.getProdutoEntity()));
                produtoPedido.setQtd(entity.getQtd());
                return produtoPedido;
            }).toList();
            pedido.setProdutosPedido(produtoPedidos);
            return pedido;
        });
    }

    @Override
    public List<Pedido> findAll(UUID uuid, boolean admin) {
        return List.of();
    }

    @Override
    public PageResult<Pedido> findAll(UUID usuarioId, boolean isAdmin, int offset, int limit, String sort) {

        // Constrói o PageRequest
        String[] sortParts = sort.split(",");
        org.springframework.data.domain.Sort.Direction direction =
                sortParts[1].equalsIgnoreCase("desc") ? org.springframework.data.domain.Sort.Direction.DESC : org.springframework.data.domain.Sort.Direction.ASC;

        org.springframework.data.domain.Pageable pageable =
                org.springframework.data.domain.PageRequest.of(offset / limit, limit, direction, sortParts[0]);

        org.springframework.data.domain.Page<PedidoEntity> pageEntities;

        if (isAdmin) {
            pageEntities = pedidoRepository.findAllWithUsuarioEntity(pageable);
        } else {
            pageEntities = pedidoRepository.findByUsuarioEntityId(usuarioId, pageable);
        }

        // Mapeia para domínio
        List<Pedido> pedidos = pageEntities.getContent().stream().map(entity -> {
            Usuario usuario = usuarioEntityMapper.toDomain(entity.getUsuarioEntity());
            Endereco endereco = EnderecoMapper.toDomain(entity.getEnderecoEntity());
            List<ArquivoMetadado> documentos = entity.getDocumentos().stream().map(ArquivoMetadadosMapper::toDomain).toList();

            Pedido pedido = PedidoMapper.toPedido(entity, usuario, new ArrayList<>(), endereco, documentos);
            List<ProdutoPedido> produtoPedidos = entity.getProdutosPedido().stream().map(produtoEntity -> {
                ProdutoPedido p = new ProdutoPedido();
                p.setPedido(pedido);
                p.setProduto(ProdutoEntityMapper.toDomain(produtoEntity.getProdutoEntity()));
                p.setQtd(produtoEntity.getQtd());
                return p;
            }).toList();
            pedido.setProdutosPedido(produtoPedidos);

            return pedido;
        }).toList();

        return new PageResult<>(pedidos, pageEntities.getTotalElements(), offset, limit);
    }
}
