package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.endereco;

public record EnderecoResumidoDTO(
        String cep,
        String logradouro,
        String numero,
        String cidade,
        String estado
) {
}
