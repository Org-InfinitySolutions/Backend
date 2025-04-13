package com.infinitysolutions.applicationservice.model.dto.endereco;

public record EnderecoResumidoDTO(
        String cep,
        String logradouro,
        String numero,
        String cidade,
        String estado
) {
}
