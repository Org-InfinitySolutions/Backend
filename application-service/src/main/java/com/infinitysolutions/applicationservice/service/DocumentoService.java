package com.infinitysolutions.applicationservice.service;

import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.usecases.usuario.BuscarUsuarioPorId;
import com.infinitysolutions.applicationservice.infrastructure.controller.DocumentosController;
import com.infinitysolutions.applicationservice.infra.exception.DocumentoInvalidoException;
import com.infinitysolutions.applicationservice.infra.exception.ErroInesperadoException;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.ArquivoMetadadosEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.UsuarioEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentoService {

    private final BuscarUsuarioPorId buscarUsuarioPorId;
    private final ArquivoMetadadosService arquivoMetadadosService;
    private final UsuarioRepository usuarioRepository;

    public void adicionarDocumentoUsuario(MultipartFile documento, DocumentosController.DadosDocumentoCriacaoDTO dto, UUID idUsuario) {
        var usuarioEncontrado = buscarUsuarioPorId.execute(idUsuario);
        UsuarioEntity usuarioEntity = usuarioRepository.findByIdAndIsAtivoTrue(idUsuario).orElseThrow(() -> RecursoNaoEncontradoException.usuarioNaoEncontrado(idUsuario));


        switch (usuarioEncontrado.getTipoUsuario()) {
            case PF -> {
                switch (dto.tipoAnexo()) {
                    case COPIA_RG, COMPROVANTE_ENDERECO -> {
                        ArquivoMetadadosEntity arquivoMetadadosEntity = arquivoMetadadosService.uploadAndPersistArquivo(documento, dto.tipoAnexo(), usuarioEntity);
                        System.out.println(arquivoMetadadosEntity.getBlobUrl());
                    }
                    default -> throw DocumentoInvalidoException.tipoAnexoInvalidoPorUsuario(dto.tipoAnexo(), usuarioEncontrado.getTipoUsuario().name());
                }
            }
            case PJ -> {
                switch (dto.tipoAnexo()) {
                    case COPIA_CNPJ, COPIA_CONTRATO_SOCIAL, COMPROVANTE_ENDERECO -> {
                        ArquivoMetadadosEntity arquivoMetadadosEntity = arquivoMetadadosService.uploadAndPersistArquivo(documento, dto.tipoAnexo(), usuarioEntity);
                        System.out.println(arquivoMetadadosEntity.getBlobUrl());
                    }
                    default -> throw DocumentoInvalidoException.tipoAnexoInvalidoPorUsuario(dto.tipoAnexo(), usuarioEncontrado.getTipoUsuario().name());
                }
            }
            default -> throw new ErroInesperadoException("Erro inesperado com o tipo do usuário");
        }
    }
}
