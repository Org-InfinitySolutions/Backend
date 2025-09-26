package com.infinitysolutions.applicationservice.core.usecases.pedido;

import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoPedido;
import com.infinitysolutions.applicationservice.core.usecases.endereco.EnderecoInput;

import java.time.LocalDateTime;
import java.util.List;

public record CadastrarPedidoInput(
        List<ProdutoPedidoInput> produtos,
        EnderecoInput enderecoInput,
        TipoPedido tipo,
        LocalDateTime dataEntrega,
        LocalDateTime dataRetirada,
        String descricao

) {
public record ProdutoPedidoInput(
        Integer produtoId,
        Integer quantidade
) {}
}

