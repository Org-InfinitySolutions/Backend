package com.infinitysolutions.applicationservice.controller;

import com.infinitysolutions.applicationservice.infra.exception.ArquivoException;
import com.infinitysolutions.applicationservice.model.dto.ComprovanteEnderecoResponseDTO;
import com.infinitysolutions.applicationservice.service.ComprovanteEnderecoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/comprovante-endereco")
@RequiredArgsConstructor
public class ComprovanteEnderecoController {

    private final ComprovanteEnderecoService comprovanteEnderecoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadComprovanteEndereco(
            @PathVariable UUID usuarioId,
            @RequestParam("arquivo") MultipartFile arquivo) {
        
        try {
            comprovanteEnderecoService.uploadComprovanteEndereco(usuarioId, arquivo);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Comprovante de endereço enviado com sucesso");
        } catch (ArquivoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<byte[]> downloadComprovanteEndereco(@PathVariable UUID usuarioId) {
        try {
            ComprovanteEnderecoResponseDTO comprovanteDTO = 
                    comprovanteEnderecoService.downloadComprovanteEndereco(usuarioId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(comprovanteDTO.getTipoArquivo()));
            headers.setContentDispositionFormData("attachment", comprovanteDTO.getNomeArquivo());
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(comprovanteDTO.getDados());
        } catch (ArquivoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @GetMapping("/info")
    public ResponseEntity<ComprovanteEnderecoResponseDTO> getInfoComprovanteEndereco(@PathVariable UUID usuarioId) {
        try {
            ComprovanteEnderecoResponseDTO comprovanteDTO = 
                    comprovanteEnderecoService.downloadComprovanteEndereco(usuarioId);
            
            // Remover os dados binários para não sobrecarregar a resposta
            comprovanteDTO.setDados(null);
            
            return ResponseEntity.ok(comprovanteDTO);
        } catch (ArquivoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @DeleteMapping
    public ResponseEntity<String> deleteComprovanteEndereco(@PathVariable UUID usuarioId) {
        // Este mét odo seria implementado no serviço para permitir a exclusão do comprovante
        // comprovanteEnderecoService.deleteComprovanteEndereco(usuarioId);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body("Funcionalidade de exclusão ainda não implementada");
    }
}