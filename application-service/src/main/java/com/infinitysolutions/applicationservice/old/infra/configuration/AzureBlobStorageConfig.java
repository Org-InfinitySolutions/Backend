package com.infinitysolutions.applicationservice.old.infra.configuration;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.PublicAccessType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AzureBlobStorageConfig {
    @Value("${spring.cloud.azure.storage.connection-string}")
    private String connectionString;

    private static final String PUBLIC_CONTAINER_NAME = "arquivospublicos";
    private static final String PRIVATE_CONTAINER_NAME = "arquivosprivados";


    @Bean
    public BlobServiceClient blobServiceClient() {
        return new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }

    @Bean(name = "publicBlobContainerClient")
    public BlobContainerClient publicBlobContainerClient() {
        BlobContainerClient blobContainerClient = blobServiceClient().getBlobContainerClient(PUBLIC_CONTAINER_NAME);
        if (!blobContainerClient.exists()) {
            blobContainerClient.create();    // Cria o container
            blobContainerClient.setAccessPolicy(PublicAccessType.BLOB, null);
            log.debug("Container público {} criado com sucesso!", PUBLIC_CONTAINER_NAME);
        }
        return blobContainerClient;
    }

    @Bean(name = "privateBlobContainerClient")
    public BlobContainerClient privateBlobContainerClient() {
        BlobContainerClient blobContainerClient = blobServiceClient().getBlobContainerClient(PRIVATE_CONTAINER_NAME);
        if (!blobContainerClient.exists()) {
            blobContainerClient.create();    // Cria o container
            log.debug("Container private {} criado com sucesso!", PUBLIC_CONTAINER_NAME);
        }
        return blobContainerClient;
    }
}
