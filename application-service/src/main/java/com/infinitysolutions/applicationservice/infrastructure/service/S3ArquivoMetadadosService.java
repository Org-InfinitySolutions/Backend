package com.infinitysolutions.applicationservice.infrastructure.service;

import com.infinitysolutions.applicationservice.infrastructure.exception.DocumentoNaoEncontradoException;
import com.infinitysolutions.applicationservice.infrastructure.exception.ErroInesperadoException;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.PedidoEntity;
import com.infinitysolutions.applicationservice.infrastructure.mapper.ArquivoMetadadosMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.ArquivoMetadadosEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.UsuarioEntity;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoAnexo;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.ProdutoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.ArquivoMetadadosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class S3ArquivoMetadadosService {    
    private final S3FileUploadService s3FileUploadService;
    private final ArquivoMetadadosRepository repository;

    private String gerarFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "-" + StringUtils.cleanPath(originalFileName);
    }

    public ArquivoMetadadosEntity uploadAndPersistArquivo(MultipartFile file, TipoAnexo tipoAnexo, ProdutoEntity produtoEntity) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo não pode estar vazio.");
        }
        String objectKey = gerarObjectKey("produtos/imagens", file.getOriginalFilename());
        String objectUrl;
        try {
            objectUrl = s3FileUploadService.uploadPublicFile(file.getInputStream(), objectKey, file.getContentType(), file.getSize());
        } catch (IOException e) {
            throw new ErroInesperadoException("Erro ao enviar o arquivo público.");
        }
        ArquivoMetadadosEntity arquivoMetadadosEntitySalvo = ArquivoMetadadosMapper.toEntity(objectKey, objectUrl, file.getOriginalFilename(), file.getContentType(), file.getSize(), tipoAnexo);
        arquivoMetadadosEntitySalvo.setProduto(produtoEntity);
        return repository.save(arquivoMetadadosEntitySalvo);
    }

    @Transactional
    public ArquivoMetadadosEntity uploadAndPersistArquivo(MultipartFile file, TipoAnexo tipoAnexo, UsuarioEntity usuarioEntity) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo não pode estar vazio.");
        }
        String folderPath = switch (tipoAnexo) {
            case COMPROVANTE_ENDERECO -> "usuarios/comprovanteEndereco";
            case COPIA_RG -> "usuarios/copiaRg";
            case COPIA_CNPJ -> "usuarios/copiaCnpj";
            case COPIA_CONTRATO_SOCIAL -> "usuarios/copiaContratoSocial";
            default -> throw new ErroInesperadoException("Erro ao enviar o arquivo");
        };
        String objectKey = gerarObjectKey(folderPath, file.getOriginalFilename());
        String objectUrl;
        try {
            objectUrl = s3FileUploadService.uploadPrivateFile(file.getInputStream(), objectKey, file.getContentType(), file.getSize());
        } catch (IOException e) {
            throw new ErroInesperadoException("Erro ao enviar o arquivo privado.");
        }
        ArquivoMetadadosEntity arquivoMetadadosEntitySalvo = ArquivoMetadadosMapper.toEntity(objectKey, objectUrl, file.getOriginalFilename(), file.getContentType(), file.getSize(), tipoAnexo);
        arquivoMetadadosEntitySalvo.setUsuario(usuarioEntity);
        return repository.save(arquivoMetadadosEntitySalvo);
    }

    @Transactional
    public ArquivoMetadadosEntity uploadAndPersistArquivo(MultipartFile file, TipoAnexo tipoAnexo, PedidoEntity pedidoEntity) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo não pode estar vazio.");
        }
        String objectKey = gerarObjectKey("pedidos/documentoAuxiliar", file.getOriginalFilename());
        String objectUrl;
        try {
            objectUrl = s3FileUploadService.uploadPrivateFile(file.getInputStream(), objectKey, file.getContentType(), file.getSize());
        } catch (IOException e) {
            throw new ErroInesperadoException("Erro ao enviar o arquivo privado.");
        }
        ArquivoMetadadosEntity arquivoMetadadosEntitySalvo = ArquivoMetadadosMapper.toEntity(objectKey, objectUrl, file.getOriginalFilename(), file.getContentType(), file.getSize(), tipoAnexo);
        arquivoMetadadosEntitySalvo.setPedido(pedidoEntity);
        return repository.save(arquivoMetadadosEntitySalvo);
    }

    @Transactional
    public void deleteArquivo(Long arquivoId) {
        ArquivoMetadadosEntity arquivoMetadadosEntity = findById(arquivoId);
        s3FileUploadService.deletarArquivo(arquivoMetadadosEntity.getBlobUrl());
        repository.delete(arquivoMetadadosEntity);
    }

    private ArquivoMetadadosEntity findById(Long arquivoId) {
        return repository.findById(arquivoId).orElseThrow(() -> DocumentoNaoEncontradoException.naoEncontradoPorId(arquivoId));
    }

    private String gerarObjectKey(String folderPath, String originalFileName) {
        String fileName = gerarFileName(originalFileName);
        return folderPath + "/" + fileName;
    }
}