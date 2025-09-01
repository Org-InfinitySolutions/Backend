package com.infinitysolutions.applicationservice.infrastructure.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComprovanteEnderecoResponseDTO {
    private UUID usuarioId;
    private String nomeArquivo;
    private String tipoArquivo;
    private LocalDateTime dataUpload;
    private byte[] dados;
}