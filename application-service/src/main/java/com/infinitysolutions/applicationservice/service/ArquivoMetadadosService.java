package com.infinitysolutions.applicationservice.service;

import com.infinitysolutions.applicationservice.infra.exception.DocumentoNaoEncontradoException;
import com.infinitysolutions.applicationservice.infra.exception.ErroInesperadoException;
import com.infinitysolutions.applicationservice.mapper.ArquivoMetadadosMapper;
import com.infinitysolutions.applicationservice.model.ArquivoMetadados;
import com.infinitysolutions.applicationservice.model.Pedido;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.enums.TipoAnexo;
import com.infinitysolutions.applicationservice.model.produto.Produto;
import com.infinitysolutions.applicationservice.repository.ArquivoMetadadosRepository;
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
    public ArquivoMetadados uploadAndPersistArquivo(MultipartFile file, TipoAnexo tipoAnexo, Produto produto) {
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
        ArquivoMetadados arquivoMetadadosSalvo = ArquivoMetadadosMapper.toEntity(blobFullName, blobUrl, file.getOriginalFilename(), file.getContentType(), file.getSize(), tipoAnexo);
        arquivoMetadadosSalvo.setProduto(produto);
        return repository.save(arquivoMetadadosSalvo);
    }


    @Transactional
    public ArquivoMetadados uploadAndPersistArquivo(MultipartFile file, TipoAnexo tipoAnexo, Usuario usuario) {
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
        ArquivoMetadados arquivoMetadadosSalvo = ArquivoMetadadosMapper.toEntity(blobFullName, blobUrl, file.getOriginalFilename(), file.getContentType(), file.getSize(), tipoAnexo);
        arquivoMetadadosSalvo.setUsuario(usuario);
        return repository.save(arquivoMetadadosSalvo);
    }

    @Transactional
    public ArquivoMetadados uploadAndPersistArquivo(MultipartFile file, TipoAnexo tipoAnexo, Pedido pedido) {
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
        ArquivoMetadados arquivoMetadadosSalvo = ArquivoMetadadosMapper.toEntity(blobFullName, blobUrl, file.getOriginalFilename(), file.getContentType(), file.getSize(), tipoAnexo);
        arquivoMetadadosSalvo.setPedido(pedido);
        return repository.save(arquivoMetadadosSalvo);
    }

    @Transactional
    public void deleteArquivo(Long arquivoId) {
        ArquivoMetadados arquivoMetadados = findById(arquivoId);
        fileUploadService.deletarArquivo(arquivoMetadados.getBlobUrl());
        repository.delete(arquivoMetadados);
    }



    @Transactional
    public ArquivoMetadados atualizarImagemProduto(MultipartFile novaImagem, Produto produto) {
        if (novaImagem.isEmpty()) {
            throw new IllegalArgumentException("O arquivo de imagem não pode estar vazio.");
        }
        
        log.info("Iniciando atualização de imagem para produto ID: {}", produto.getId());
        
        // Buscar as imagens existentes do produto para excluir do blob storage
        List<ArquivoMetadados> imagensExistentes = repository.findByProduto(produto);
        
        // Excluir as imagens antigas do blob storage
        for (ArquivoMetadados imagemAntiga : imagensExistentes) {
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
        return uploadAndPersistArquivo(novaImagem, TipoAnexo.IMAGEM_PRODUTO, produto);
    }

    private ArquivoMetadados findById(Long arquivoId) {
        return repository.findById(arquivoId).orElseThrow(() -> DocumentoNaoEncontradoException.naoEncontradoPorId(arquivoId));
    }




}
