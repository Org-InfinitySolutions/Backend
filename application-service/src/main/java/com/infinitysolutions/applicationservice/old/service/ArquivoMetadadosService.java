package com.infinitysolutions.applicationservice.old.service;

import com.infinitysolutions.applicationservice.old.infra.exception.DocumentoNaoEncontradoException;
import com.infinitysolutions.applicationservice.old.infra.exception.ErroInesperadoException;
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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class ArquivoMetadadosService {    
    private final FileUploadService fileUploadService;
    private final ArquivoMetadadosRepository repository;

    private String gerarFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "-" + StringUtils.cleanPath(originalFileName);
    }

    @Transactional
    public ArquivoMetadadosEntity uploadAndPersistArquivo(MultipartFile file, TipoAnexo tipoAnexo, ProdutoEntity produtoEntity) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo não pode estar vazio.");
        }
        String blobFileName = gerarFileName(file.getOriginalFilename());
        String blobFolderName = "produtos/imagens";
        String blobFullName = blobFolderName + "/" + blobFileName;
        String blobUrl;
        try {
            blobUrl = fileUploadService.uploadPublicFile(file.getInputStream(), blobFullName, file.getContentType(), file.getSize());
        } catch (IOException e) {
            throw new ErroInesperadoException("Erro ao enviar o arquivo privado.");
        }
        ArquivoMetadadosEntity arquivoMetadadosEntitySalvo = ArquivoMetadadosMapper.toEntity(blobFullName, blobUrl, file.getOriginalFilename(), file.getContentType(), file.getSize(), tipoAnexo);
        arquivoMetadadosEntitySalvo.setProduto(produtoEntity);
        return repository.save(arquivoMetadadosEntitySalvo);
    }


    @Transactional
    public ArquivoMetadadosEntity uploadAndPersistArquivo(MultipartFile file, TipoAnexo tipoAnexo, UsuarioEntity usuarioEntity) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo não pode estar vazio.");
        }
        String blobFileName = gerarFileName(file.getOriginalFilename());
        String blobFolderName = switch (tipoAnexo) {
            case COMPROVANTE_ENDERECO -> "usuarios/comprovanteEndereco";
            case COPIA_RG -> "usuarios/copiaRg";
            case COPIA_CNPJ -> "usuarios/copiaCnpj";
            case COPIA_CONTRATO_SOCIAL -> "usuarios/copiaContratoSocial";
            default -> throw new ErroInesperadoException("Erro ao enviar o arquivo");
        };
        String blobFullName = blobFolderName + "/" + blobFileName;
        String blobUrl;
        try {
            blobUrl = fileUploadService.uploadPrivateFile(file.getInputStream(), blobFullName, file.getContentType(), file.getSize());
        } catch (IOException e) {
            throw new ErroInesperadoException("Erro ao enviar o arquivo privado.");
        }
        ArquivoMetadadosEntity arquivoMetadadosEntitySalvo = ArquivoMetadadosMapper.toEntity(blobFullName, blobUrl, file.getOriginalFilename(), file.getContentType(), file.getSize(), tipoAnexo);
        arquivoMetadadosEntitySalvo.setUsuario(usuarioEntity);
        return repository.save(arquivoMetadadosEntitySalvo);
    }

    @Transactional
    public ArquivoMetadadosEntity uploadAndPersistArquivo(MultipartFile file, TipoAnexo tipoAnexo, PedidoEntity pedidoEntity) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo não pode estar vazio.");
        }
        String blobFileName = gerarFileName(file.getOriginalFilename());
        String blobFolderName = "pedidos/documentoAuxiliar";
        String blobFullName = blobFolderName + "/" + blobFileName;
        String blobUrl;
        try {
            blobUrl = fileUploadService.uploadPrivateFile(file.getInputStream(), blobFullName, file.getContentType(), file.getSize());
        } catch (IOException e) {
            throw new ErroInesperadoException("Erro ao enviar o arquivo privado.");
        }
        ArquivoMetadadosEntity arquivoMetadadosEntitySalvo = ArquivoMetadadosMapper.toEntity(blobFullName, blobUrl, file.getOriginalFilename(), file.getContentType(), file.getSize(), tipoAnexo);
        arquivoMetadadosEntitySalvo.setPedido(pedidoEntity);
        return repository.save(arquivoMetadadosEntitySalvo);
    }

    @Transactional
    public void deleteArquivo(Long arquivoId) {
        ArquivoMetadadosEntity arquivoMetadadosEntity = findById(arquivoId);
        fileUploadService.deletarArquivo(arquivoMetadadosEntity.getBlobUrl());
        repository.delete(arquivoMetadadosEntity);
    }



    @Transactional
    public ArquivoMetadadosEntity atualizarImagemProduto(MultipartFile novaImagem, ProdutoEntity produtoEntity) {
        if (novaImagem.isEmpty()) {
            throw new IllegalArgumentException("O arquivo de imagem não pode estar vazio.");
        }
        
        log.info("Iniciando atualização de imagem para produto ID: {}", produtoEntity.getId());
        
        // Buscar as imagens existentes do produto para excluir do blob storage
        List<ArquivoMetadadosEntity> imagensExistentes = repository.findByProduto(produtoEntity);
        
        // Excluir as imagens antigas do blob storage
        for (ArquivoMetadadosEntity imagemAntiga : imagensExistentes) {
            try {
                fileUploadService.deletarArquivo(imagemAntiga.getBlobUrl());
                repository.delete(imagemAntiga);
                log.info("Arquivo excluído do blob storage: {}", imagemAntiga.getBlobName());
            } catch (Exception e) {
                log.warn("Erro ao excluir arquivo do blob storage: {}, erro: {}", imagemAntiga.getBlobName(), e.getMessage());
            }
        }
        
        // Fazer upload da nova imagem
        log.info("Fazendo upload da nova imagem: {}", novaImagem.getOriginalFilename());
        return uploadAndPersistArquivo(novaImagem, TipoAnexo.IMAGEM_PRODUTO, produtoEntity);
    }

    private ArquivoMetadadosEntity findById(Long arquivoId) {
        return repository.findById(arquivoId).orElseThrow(() -> DocumentoNaoEncontradoException.naoEncontradoPorId(arquivoId));
    }




}
