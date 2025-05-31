package com.infinitysolutions.applicationservice.service;

import com.azure.core.exception.AzureException;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobStorageException;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.infinitysolutions.applicationservice.infra.exception.DocumentoNaoEncontradoException;
import com.infinitysolutions.applicationservice.infra.exception.ErroInesperadoException;
import com.infinitysolutions.applicationservice.infra.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class FileUploadService {

    private final BlobContainerClient publicBlobContainerClient;

    private final BlobContainerClient privateBlobContainerClient;

    public FileUploadService(
            @Qualifier("publicBlobContainerClient") BlobContainerClient publicBlobContainerClient,
            @Qualifier("privateBlobContainerClient") BlobContainerClient privateBlobContainerClient
    ) {
        this.publicBlobContainerClient = publicBlobContainerClient;
        this.privateBlobContainerClient = privateBlobContainerClient;
    }

    /**
     * Faz upload de um arquivo para o contêiner de arquivos PÚBLICOS.
     * @param inputStream O conteúdo do arquivo.
     * @param blobName O nome completo do blob (ex: "pasta/nome_unico_do_arquivo.jpg").
     * @param contentType O tipo de conteúdo (MIME type).
     * @param fileSize O tamanho do arquivo em bytes (obtido do MultipartFile.getSize()).
     * @return A URL pública direta do arquivo.
     * @throws BlobStorageException Se houver erro na comunicação com o Azure Blob Storage.
     * @throws IllegalArgumentException Se parâmetros inválidos forem fornecidos.
     * @throws RuntimeException Para erros inesperados.
     */
    public String uploadPublicFile(InputStream inputStream, String blobName, String contentType, long fileSize) {
        try {
            return performUpload(publicBlobContainerClient, inputStream, blobName, contentType, fileSize);
        } catch (BlobStorageException e) {
            log.error("Erro ao comunicar com Azure Blob Storage (Público): {}", e.getMessage(), e);
            throw new ErroInesperadoException("Erro de comunicação com o Azure Blob Storage (Público).");
        } catch (IOException e) {
            log.error("Erro de I/O ao processar o arquivo (Público): {}", e.getMessage(), e);
            throw new ErroInesperadoException("Erro de I/O ao processar o arquivo (Público).");
        } catch (AzureException e) {
            log.error("Erro ao comunicar com Azure Blob Storage: {}", e.getMessage(), e);
            throw new ErroInesperadoException("Erro de comunicação com o Azure Blob Storage.");
        } catch (Exception e) {
            log.error("Erro inesperado durante upload de arquivo (Público): {}", e.getMessage(), e);
            throw new ErroInesperadoException("Erro inesperado durante upload de arquivo (Público): " + e.getMessage());
        }
    }

    /**
     * Faz upload de um arquivo para o contêiner de arquivos PRIVADOS.
     * @param inputStream O conteúdo do arquivo.
     * @param blobName O nome completo do blob (ex: "pasta/nome_unico_do_arquivo.pdf").
     * @param contentType O tipo de conteúdo (MIME type).
     * @param fileSize O tamanho do arquivo em bytes (obtido do MultipartFile.getSize()).
     * @return A URL direta do arquivo (que precisará de SAS para acesso, pois está no container privado).
     * @throws BlobStorageException Se houver erro na comunicação com o Azure Blob Storage.
     * @throws IllegalArgumentException Se parâmetros inválidos forem fornecidos.
     * @throws RuntimeException Para erros inesperados.
     */
    public String uploadPrivateFile(InputStream inputStream, String blobName, String contentType, long fileSize){
        try {
            return performUpload(privateBlobContainerClient, inputStream, blobName, contentType, fileSize);
        } catch (BlobStorageException e) {
            log.error("Erro ao comunicar com Azure Blob Storage (Público): {}", e.getMessage(), e);
            throw new ErroInesperadoException("Erro de comunicação com o Azure Blob Storage (Público).");
        } catch (IOException e) {
            log.error("Erro de I/O ao processar o arquivo (Público): {}", e.getMessage(), e);
            throw new ErroInesperadoException("Erro de I/O ao processar o arquivo (Público).");
        } catch (AzureException e) {
            log.error("Erro ao comunicar com Azure Blob Storage: {}", e.getMessage(), e);
            throw new ErroInesperadoException("Erro de comunicação com o Azure Blob Storage.");
        } catch (Exception e) {
            log.error("Erro inesperado durante upload de arquivo (Público): {}", e.getMessage(), e);
            throw new ErroInesperadoException("Erro inesperado durante upload de arquivo (Público): " + e.getMessage());
        }
    }


    private String performUpload(BlobContainerClient containerClient, InputStream inputStream, String blobName, String contentType, long fileSize) throws IOException {
        Objects.requireNonNull(inputStream, "InputStream não pode ser nulo");
        if (!StringUtils.hasText(blobName)) { // blobName agora é o caminho final (ex: "pasta/arquivo.pdf")
            throw new IllegalArgumentException("Nome do blob não pode ser vazio.");
        }
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        BlobHttpHeaders headers = new BlobHttpHeaders();
        if (StringUtils.hasText(contentType)) {
            headers.setContentType(contentType);
        } else {
            // Fallback para tipo de conteúdo genérico se não for fornecido
            headers.setContentType("application/octet-stream");
        }

        blobClient.upload(inputStream, fileSize, true); // O 'true' indica sobrescrever se o blob já existir
        blobClient.setHttpHeaders(headers); // Define os headers após o upload

        log.info("Arquivo '{}' enviado com sucesso para container '{}' como '{}' com tipo de conteúdo '{}'",
                 blobName, containerClient.getBlobContainerName(), blobName, contentType);
        return blobClient.getBlobUrl();
    }

    public void deletarArquivo(String blobUrl) {
    }

    /**
     * Retorna a URL direta de um blob localizado no contêiner de arquivos PÚBLICOS.
     * Esta URL é permanente e não expira (assumindo que o container está configurado para acesso público).
     * @param blobName O nome único do blob (ex: "pasta/nome_unico_do_arquivo.jpg").
     * @return A URL pública direta do blob.
     * @throws BlobStorageException se o blob não for encontrado ou houver erro de comunicação.
     */
    public String getPublicFileUrl(String blobName) {
        try {
            BlobClient blobClient = publicBlobContainerClient.getBlobClient(blobName);

             if (!blobClient.exists()) {
                 throw new RecursoNaoEncontradoException("Blob público não encontrado: " + blobName);
             }
            return blobClient.getBlobUrl();
        } catch (BlobStorageException e) {
            log.error("Erro ao obter URL de blob público '{}': {}", blobName, e.getMessage());
            throw new ErroInesperadoException("Erro inesperado ao obter url do arquivo público.");
        }
    }

    /**
     * Gera uma URL com Shared Access Signature (SAS Token) para acesso temporário e seguro a um blob
     * localizado no contêiner de arquivos PRIVADOS.
     * @param blobName O nome único do blob.
     * @param expiryMinutes Quantos minutos a URL gerada será válida.
     * @return A URL do blob com o SAS Token anexado.
     * @throws BlobStorageException se o blob não for encontrado ou houver erro de comunicação.
     */
    public String generatePrivateFileSasUrl(String blobName, int expiryMinutes) {
        try {
            BlobClient blobClient = privateBlobContainerClient.getBlobClient(blobName);
            if (!blobClient.exists()) {
                throw DocumentoNaoEncontradoException.naoEncontradoPorNome(blobName);
            }

            // Definir permissões: Apenas leitura ('r')
            BlobSasPermission sasPermission = new BlobSasPermission().setReadPermission(true);

            // Definir o tempo de expiração da URL SAS
            OffsetDateTime expiryTime = OffsetDateTime.now().plusMinutes(expiryMinutes);

            // Criar os valores da assinatura SAS
            BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(expiryTime, sasPermission)
                    .setStartTime(OffsetDateTime.now().minusMinutes(5));

            // Gerar o token SAS
            String sasToken = blobClient.generateSas(sasValues);

            // Combinar a URL direta do blob com o token SAS
            String s = blobClient.getBlobUrl() + "?" + sasToken;

            log.debug("url completa {}", s);
            return s;
        } catch (BlobStorageException e) {
            log.error("Erro ao gerar SAS URL para blob privado '{}': {}", blobName, e.getMessage());
            throw new ErroInesperadoException("Erro ao gerar SAS URL para blob privado.");
        }
    }
}
