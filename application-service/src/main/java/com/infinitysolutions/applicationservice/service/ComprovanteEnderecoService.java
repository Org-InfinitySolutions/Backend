package com.infinitysolutions.applicationservice.service;

import com.infinitysolutions.applicationservice.infra.exception.ArquivoException;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.dto.ComprovanteEnderecoResponseDTO;
import com.infinitysolutions.applicationservice.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComprovanteEnderecoService {

    private final UsuarioRepository usuarioRepository;
    
    private static final String[] TIPOS_PERMITIDOS = {
            "image/jpeg", "image/png", "image/jpg", "application/pdf"
    };
    
    private static final long TAMANHO_MAXIMO = 5 * 1024 * 1024; // 5MB
    
    @Transactional
    public void uploadComprovanteEndereco(UUID usuarioId, MultipartFile arquivo) {
        validarArquivo(arquivo);
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ArquivoException("Usuário não encontrado com ID: " + usuarioId));
        
        try {
            usuario.setComprovanteEndereco(arquivo.getBytes());
            usuario.setComprovanteNomeArquivo(arquivo.getOriginalFilename());
            usuario.setComprovanteTipoArquivo(arquivo.getContentType());
            usuario.setComprovanteDataUpload(LocalDateTime.now());
            
            usuarioRepository.save(usuario);
        } catch (IOException e) {
            throw new ArquivoException("Erro ao processar o arquivo", e);
        }
    }
    
    @Transactional(readOnly = true)
    public ComprovanteEnderecoResponseDTO downloadComprovanteEndereco(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ArquivoException("Usuário não encontrado com ID: " + usuarioId));
        
        if (usuario.getComprovanteEndereco() == null) {
            throw new ArquivoException("Este usuário não possui comprovante de endereço cadastrado");
        }
        
        return new ComprovanteEnderecoResponseDTO(
                usuario.getId(),
                usuario.getComprovanteNomeArquivo(),
                usuario.getComprovanteTipoArquivo(),
                usuario.getComprovanteDataUpload(),
                usuario.getComprovanteEndereco()
        );
    }
    
    private void validarArquivo(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new ArquivoException("Arquivo não pode estar vazio");
        }
        
        if (arquivo.getSize() > TAMANHO_MAXIMO) {
            throw new ArquivoException("Tamanho do arquivo excede o limite máximo de 5MB");
        }
        
        String tipoArquivo = arquivo.getContentType();
        boolean tipoPermitido = false;
        
        for (String tipo : TIPOS_PERMITIDOS) {
            if (tipo.equals(tipoArquivo)) {
                tipoPermitido = true;
                break;
            }
        }
        
        if (!tipoPermitido) {
            throw new ArquivoException("Tipo de arquivo não permitido. Apenas JPEG, PNG, JPG e PDF são aceitos");
        }
    }
}