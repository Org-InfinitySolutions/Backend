package com.infinitysolutions.applicationservice.model.dto;

public record EnderecoResumidoDTO(
        String cep,
        String logradouro,
        String numero,
        String cidade,
        String estado
) {
}
