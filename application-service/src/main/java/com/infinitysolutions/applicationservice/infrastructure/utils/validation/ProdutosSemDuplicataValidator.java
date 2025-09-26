package com.infinitysolutions.applicationservice.infrastructure.utils.validation;

import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pedido.PedidoCadastroDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProdutosSemDuplicataValidator implements ConstraintValidator<ProdutosSemDuplicata, List<PedidoCadastroDTO.ProdutoPedidoDTO>> {

    @Override
    public boolean isValid(List<PedidoCadastroDTO.ProdutoPedidoDTO> produtos, ConstraintValidatorContext context) {
        if (produtos == null || produtos.isEmpty()) return true;

        Set<Integer> produtosIds = new HashSet<>();
        for (PedidoCadastroDTO.ProdutoPedidoDTO produto : produtos) {
            if (!produtosIds.add(produto.produtoId())) {
                return false;
            }
        }
        return true;
    }
}
