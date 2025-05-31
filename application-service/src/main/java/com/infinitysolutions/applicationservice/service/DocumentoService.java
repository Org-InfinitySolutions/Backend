package com.infinitysolutions.applicationservice.service;

import com.infinitysolutions.applicationservice.controller.DocumentosController;
import com.infinitysolutions.applicationservice.infra.exception.DocumentoInvalidoException;
import com.infinitysolutions.applicationservice.infra.exception.ErroInesperadoException;
import com.infinitysolutions.applicationservice.infra.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.model.ArquivoMetadados;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.enums.TipoAnexo;
import com.infinitysolutions.applicationservice.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentoService {

    private final UsuarioService usuarioService;
    private final ArquivoMetadadosService arquivoMetadadosService;
    private final UsuarioRepository usuarioRepository;

    public void adicionarDocumentoUsuario(MultipartFile documento, DocumentosController.DadosDocumentoCriacaoDTO dto, UUID idUsuario) {
        var usuarioEncontrado = usuarioService.buscarPorId(idUsuario);
        Usuario usuario = usuarioRepository.findByIdAndIsAtivoTrue(idUsuario).orElseThrow(() -> RecursoNaoEncontradoException.usuarioNaoEncontrado(idUsuario));


        switch (usuarioEncontrado.getTipo()) {
            case "PF" -> {
                switch (dto.tipoAnexo()) {
                    case COPIA_RG, COMPROVANTE_ENDERECO -> {
                        ArquivoMetadados arquivoMetadados = arquivoMetadadosService.uploadAndPersistArquivo(documento, dto.tipoAnexo(), usuario);
                        System.out.println(arquivoMetadados.getBlobUrl());
                    }
                    default -> throw DocumentoInvalidoException.tipoAnexoInvalidoPorUsuario(dto.tipoAnexo(), usuarioEncontrado.getTipo());
                }
            }
            case "PJ" -> {
                switch (dto.tipoAnexo()) {
                    case COPIA_CNPJ, COPIA_CONTRATO_SOCIAL, COMPROVANTE_ENDERECO -> {
                        ArquivoMetadados arquivoMetadados = arquivoMetadadosService.uploadAndPersistArquivo(documento, dto.tipoAnexo(), usuario);
                        System.out.println(arquivoMetadados.getBlobUrl());
                    }
                    default -> throw DocumentoInvalidoException.tipoAnexoInvalidoPorUsuario(dto.tipoAnexo(), usuarioEncontrado.getTipo());
                }
            }
            default -> throw new ErroInesperadoException("Erro inesperado com o tipo do usuário");
        }
    }
}
