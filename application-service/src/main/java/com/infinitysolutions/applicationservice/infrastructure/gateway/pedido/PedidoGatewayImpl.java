package com.infinitysolutions.applicationservice.infrastructure.gateway.pedido;

import com.infinitysolutions.applicationservice.core.domain.pedido.Pedido;
import com.infinitysolutions.applicationservice.core.domain.pedido.ProdutoPedido;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.gateway.PedidoGateway;
import com.infinitysolutions.applicationservice.infrastructure.mapper.PedidoMapper;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoGatewayImpl implements PedidoGateway {

    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    @Override
    public Pedido save(Pedido novoPedido) {

        UsuarioEntity usuarioEntity = usuarioRepository.findByIdAndIsAtivoTrue(novoPedido.getUsuario().getId()).orElseThrow(() -> RecursoNaoEncontradoException.usuarioNaoEncontrado(novoPedido.getUsuario().getId()));
        EnderecoEntity enderecoEntity = enderecoRepository.findById(novoPedido.getEndereco().getId()).orElseThrow(() -> RecursoNaoEncontradoException.enderecoNaoEncontrado(novoPedido.getEndereco().getId()));

        // If the pedido already has an ID, we need to fetch it to avoid creating a duplicate
        PedidoEntity pedidoEntity;
        if (novoPedido.getId() != null) {
            pedidoEntity = pedidoRepository.findById(novoPedido.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado com ID: " + novoPedido.getId()));
        } else {
            // Create a new pedido entity
            pedidoEntity = PedidoMapper.toEntity(
                    usuarioEntity, new ArrayList<>(), enderecoEntity, novoPedido.getSituacao(), novoPedido.getTipo(), novoPedido.getDataCriacao(),
                    novoPedido.getDataAtualizacao(), novoPedido.getDataAprovacao(), novoPedido.getDataInicioEvento(), novoPedido.getDataFinalizacao(),
                    novoPedido.getDataCancelamento(), novoPedido.getDataEntrega(), novoPedido.getDataRetirada(), novoPedido.getDescricao(), new ArrayList<>()
            );

            // Save the entity to get an ID
            pedidoEntity = pedidoRepository.save(pedidoEntity);
        }

        // Create or update produto pedido entities
        List<ProdutoPedidoEntity> produtoPedidoEntityList = new ArrayList<>();
        for (ProdutoPedido produtoPedido : novoPedido.getProdutosPedido()) {
            ProdutoPedidoEntity produtoPedidoEntity = new ProdutoPedidoEntity();
            produtoPedidoEntity.setPedidoEntity(pedidoEntity);
            produtoPedidoEntity.setProdutoEntity(produtoRepository.getReferenceById(produtoPedido.getProduto().getId()));
            produtoPedidoEntity.setQtd(produtoPedido.getQtd());
            produtoPedidoEntityList.add(produtoPedidoEntity);
        }

        // Update the entity with the product list
        pedidoEntity.setProdutosPedido(produtoPedidoEntityList);

        // No need to create a new entity, just update the existing one
        PedidoEntity savedEntity = pedidoRepository.save(pedidoEntity);

        return PedidoMapper.toPedido(savedEntity, novoPedido.getUsuario(), novoPedido.getProdutosPedido(), novoPedido.getEndereco(), novoPedido.getDocumentos());
    }

}
