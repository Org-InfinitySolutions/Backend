package com.infinitysolutions.applicationservice.infrastructure.service;

import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.infrastructure.exception.ErroInesperadoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3FileUploadService {

    private final S3Client s3Client;
    private final String publicBucketName;
    private final String privateBucketName;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.endpoint:}")
    private String endpoint;

    @Value("${aws.s3.use-localstack:false}")
    private boolean useLocalstack;

    /**
     * Faz upload de um arquivo para o bucket PÚBLICO.
     * @param inputStream O conteúdo do arquivo.
     * @param objectKey A chave completa do objeto (ex: "pasta/nome_unico_do_arquivo.jpg").
     * @param contentType O tipo de conteúdo (MIME type).
     * @param fileSize O tamanho do arquivo em bytes.
     * @return A URL pública direta do arquivo.
     */
    public String uploadPublicFile(InputStream inputStream, String objectKey, String contentType, long fileSize) {
        try {
            return performUpload(publicBucketName, inputStream, objectKey, contentType, fileSize);
        } catch (Exception e) {
            log.error("Erro inesperado durante upload de arquivo público: {}", e.getMessage(), e);
            throw new ErroInesperadoException("Erro inesperado durante upload de arquivo público: " + e.getMessage());
        }
    }

    /**
     * Faz upload de um arquivo para o bucket PRIVADO.
     * @param inputStream O conteúdo do arquivo.
     * @param objectKey A chave completa do objeto (ex: "pasta/nome_unico_do_arquivo.pdf").
     * @param contentType O tipo de conteúdo (MIME type).
     * @param fileSize O tamanho do arquivo em bytes.
     * @return A URL direta do arquivo (que precisará de presigned URL para acesso).
     */
    public String uploadPrivateFile(InputStream inputStream, String objectKey, String contentType, long fileSize) {
        try {
            return performUpload(privateBucketName, inputStream, objectKey, contentType, fileSize);
        } catch (Exception e) {
            log.error("Erro inesperado durante upload de arquivo privado: {}", e.getMessage(), e);
            throw new ErroInesperadoException("Erro inesperado durante upload de arquivo privado: " + e.getMessage());
        }
    }

    private String performUpload(String bucketName, InputStream inputStream, String objectKey, String contentType, long fileSize) throws IOException {
        Objects.requireNonNull(inputStream, "InputStream não pode ser nulo");
        if (!StringUtils.hasText(objectKey)) {
            throw new IllegalArgumentException("Object key não pode ser vazio.");
        }

        try {
            PutObjectRequest.Builder requestBuilder = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .contentLength(fileSize);

            if (StringUtils.hasText(contentType)) {
                requestBuilder.contentType(contentType);
            } else {
                requestBuilder.contentType("application/octet-stream");
            }

            PutObjectRequest request = requestBuilder.build();
            
            s3Client.putObject(request, RequestBody.fromInputStream(inputStream, fileSize));

            log.info("Arquivo '{}' enviado com sucesso para bucket '{}' com tipo de conteúdo '{}'",
                    objectKey, bucketName, contentType);

            return buildObjectUrl(bucketName, objectKey);
        } catch (S3Exception e) {
            log.error("Erro ao comunicar com S3: {}", e.getMessage(), e);
            throw new ErroInesperadoException("Erro de comunicação com o S3.");
        } catch (Exception e) {
            log.error("Erro de I/O ao processar o arquivo: {}", e.getMessage(), e);
            throw new ErroInesperadoException("Erro de I/O ao processar o arquivo.");
        }
    }

    /**
     * Deleta um arquivo do S3.
     * Determina automaticamente se o arquivo está no bucket público ou privado baseado na URL.
     * @param objectUrl A URL completa do objeto a ser deletado.
     */
    public void deletarArquivo(String objectUrl) {
        if (!StringUtils.hasText(objectUrl)) {
            log.warn("URL do objeto está vazia, não há arquivo para deletar.");
            return;
        }

        try {
            String objectKey = extractObjectKeyFromUrl(objectUrl);
            String bucketName = extractBucketNameFromUrl(objectUrl);

            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            s3Client.deleteObject(request);
            log.info("Arquivo '{}' deletado com sucesso do bucket '{}'", objectKey, bucketName);

        } catch (S3Exception e) {
            log.error("Erro ao deletar arquivo do S3. URL: {}, Erro: {}", objectUrl, e.getMessage());
            throw new ErroInesperadoException("Erro ao deletar arquivo do S3: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro inesperado ao deletar arquivo. URL: {}, Erro: {}", objectUrl, e.getMessage());
            throw new ErroInesperadoException("Erro inesperado ao deletar arquivo: " + e.getMessage());
        }
    }

    /**
     * Retorna a URL direta de um objeto localizado no bucket PÚBLICO.
     * @param objectKey A chave do objeto.
     * @return A URL pública direta do objeto.
     */
    public String getPublicFileUrl(String objectKey) {
        try {
            if (!objectExists(publicBucketName, objectKey)) {
                throw new RecursoNaoEncontradoException("Objeto público não encontrado: " + objectKey);
            }
            return buildObjectUrl(publicBucketName, objectKey);
        } catch (S3Exception e) {
            log.error("Erro ao obter URL de objeto público '{}': {}", objectKey, e.getMessage());
            throw new ErroInesperadoException("Erro inesperado ao obter url do arquivo público.");
        }
    }

    /**
     * Gera uma URL pré-assinada para acesso temporário e seguro a um objeto
     * localizado no bucket PRIVADO.
     * @param objectKey A chave do objeto.
     * @param expiryMinutes Quantos minutos a URL gerada será válida.
     * @return A URL do objeto com pré-assinatura.
     */
    public String generatePrivateFilePresignedUrl(String objectKey, int expiryMinutes) {
        try {
            if (!objectExists(privateBucketName, objectKey)) {
                throw new RecursoNaoEncontradoException("Objeto privado não encontrado: " + objectKey);
            }
             GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(privateBucketName)
                    .key(objectKey)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(expiryMinutes))
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            String presignedUrl = presignedRequest.url().toString();

            log.debug("URL pré-assinada gerada para {}: {}", objectKey, presignedUrl);
            return presignedUrl;
          
        } catch (S3Exception e) {
            log.error("Erro ao gerar URL pré-assinada para objeto privado '{}': {}", objectKey, e.getMessage());
            throw new ErroInesperadoException("Erro ao gerar URL pré-assinada para objeto privado.");
        }
    }

    private boolean objectExists(String bucketName, String objectKey) {
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    private String buildObjectUrl(String bucketName, String objectKey) {
        if (useLocalstack && !endpoint.isEmpty()) {
            // Para LocalStack: http://localhost:4566/bucket/key
            return String.format("%s/%s/%s", endpoint, bucketName, objectKey);
        } else {
            // Para AWS real: https://bucket.s3.region.amazonaws.com/key
            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, objectKey);
        }
    }

    private String extractObjectKeyFromUrl(String objectUrl) {
        try {
            if (useLocalstack && objectUrl.contains(endpoint)) {
                // Para LocalStack: http://localhost:4566/bucket/path/to/file.jpg
                String[] parts = objectUrl.replace(endpoint + "/", "").split("/", 2);
                return parts.length > 1 ? parts[1] : "";
            } else {
                // Para AWS: https://bucket.s3.amazonaws.com/path/to/file.jpg
                String[] parts = objectUrl.split(".s3.amazonaws.com/", 2);
                return parts.length > 1 ? parts[1] : "";
            }
        } catch (Exception e) {
            log.error("Erro ao extrair object key da URL: {}", objectUrl);
            throw new IllegalArgumentException("Não foi possível extrair a object key da URL: " + objectUrl, e);
        }
    }

    private String extractBucketNameFromUrl(String objectUrl) {
        try {
            if (useLocalstack && objectUrl.contains(endpoint)) {
                // Para LocalStack: http://localhost:4566/bucket/path/to/file.jpg
                String pathPart = objectUrl.replace(endpoint + "/", "");
                return pathPart.split("/")[0];
            } else {
                // Para AWS: https://bucket.s3.amazonaws.com/path/to/file.jpg
                String[] parts = objectUrl.split("\\.");
                return parts[0].replace("https://", "");
            }
        } catch (Exception e) {
            log.error("Erro ao extrair bucket name da URL: {}", objectUrl);
            throw new IllegalArgumentException("Não foi possível extrair o bucket name da URL: " + objectUrl, e);
        }
    }

    /**
     * Determina se um arquivo é público baseado na object key.
     * @param objectKey A chave do objeto.
     * @return true se o arquivo é público, false se é privado.
     */
    private boolean isArquivoPublico(String objectKey) {
        // Arquivos públicos são apenas imagens de produtos
        return objectKey.startsWith("produtos/imagens");
    }
}