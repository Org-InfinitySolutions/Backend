package com.infinitysolutions.applicationservice.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutBucketPolicyRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
@Slf4j
public class AwsS3Config {

    @Value("${aws.s3.region:us-east-1}")
    private String region;

    @Value("${aws.s3.access-key:}")
    private String accessKey;

    @Value("${aws.s3.secret-key:}")
    private String secretKey;

    @Value("${aws.s3.endpoint:}")
    private String endpoint;

    @Value("${aws.s3.public-bucket-name:arquivos-publicos}")
    private String publicBucketName;

    @Value("${aws.s3.private-bucket-name:arquivos-privados}")
    private String privateBucketName;

    @Value("${aws.s3.use-localstack:false}")
    private boolean useLocalstack;

    @Bean
    public S3Client s3Client() {
        S3ClientBuilder clientBuilder = S3Client.builder()
                .region(Region.of(region));

        // Para ambiente de desenvolvimento com LocalStack ou credenciais explícitas
        if (useLocalstack || (!accessKey.isEmpty() && !secretKey.isEmpty())) {
            AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
            clientBuilder.credentialsProvider(StaticCredentialsProvider.create(credentials));
            
            if (!endpoint.isEmpty()) {
                clientBuilder.endpointOverride(URI.create(endpoint));
                // Para LocalStack, forçar path-style access
                if (useLocalstack) {
                    clientBuilder.forcePathStyle(true);
                }
            }
        } else {
            log.info("Usando credenciais padrão da AWS.");
            clientBuilder.credentialsProvider(DefaultCredentialsProvider.create());
        }

        S3Client s3Client = clientBuilder.build();
        
        // Criar buckets se não existirem
        createBucketIfNotExists(s3Client, publicBucketName, true);
        createBucketIfNotExists(s3Client, privateBucketName, false);
        
        return s3Client;
    }

    @Bean
    public S3Presigner s3Presigner() {
        S3Presigner.Builder presignerBuilder = S3Presigner.builder()
                .region(Region.of(region));

        if (useLocalstack || (!accessKey.isEmpty() && !secretKey.isEmpty())) {
            AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
            presignerBuilder.credentialsProvider(StaticCredentialsProvider.create(credentials));
            
            if (!endpoint.isEmpty()) {
                presignerBuilder.endpointOverride(URI.create(endpoint));
                if (useLocalstack) {
                    presignerBuilder.serviceConfiguration(
                        S3Configuration.builder().pathStyleAccessEnabled(true).build()
                    );
                }
            }
        } else {
            log.info("Usando credenciais padrão da AWS.");
            presignerBuilder.credentialsProvider(DefaultCredentialsProvider.create());
        }

        return presignerBuilder.build();
    }

    @Bean
    public String publicBucketName() {
        return publicBucketName;
    }

    @Bean
    public String privateBucketName() {
        return privateBucketName;
    }

    private void createBucketIfNotExists(S3Client s3Client, String bucketName, boolean isPublic) {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
            log.debug("Bucket {} já existe", bucketName);
        } catch (NoSuchBucketException e) {
            try {
                s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
                log.info("Bucket {} criado com sucesso", bucketName);
                
                if (isPublic) {
                    configureBucketAsPublic(s3Client, bucketName);
                } else {
                    configureBucketAsPrivate(s3Client, bucketName);
                }
            } catch (Exception ex) {
                log.error("Erro ao criar bucket {}: {}", bucketName, ex.getMessage());
            }
        } catch (Exception e) {
            log.error("Erro ao verificar bucket {}: {}", bucketName, e.getMessage());
        }
    }

    private void configureBucketAsPublic(S3Client s3Client, String bucketName) {
        try {
            String publicReadPolicy = """
                {
                    "Version": "2012-10-17",
                    "Statement": [
                        {
                            "Sid": "PublicReadGetObject",
                            "Effect": "Allow",
                            "Principal": "*",
                            "Action": "s3:GetObject",
                            "Resource": "arn:aws:s3:::arquivos-publicos-dev/*"
                        }
                    ]
                }
                """.formatted(bucketName);

            s3Client.putBucketPolicy(PutBucketPolicyRequest.builder()
                    .bucket(bucketName)
                    .policy(publicReadPolicy)
                    .build());
            
            log.info("Bucket {} configurado como público", bucketName);
        } catch (Exception e) {
            log.warn("Não foi possível configurar bucket {} como público: {}", bucketName, e.getMessage());
        }
    }

    private void configureBucketAsPrivate(S3Client s3Client, String bucketName) {
        try {
            String publicReadPolicy = """
                {
                    "Version": "2012-10-17",
                    "Statement": [
                        {
                            "Sid": "DenyPublicAccess",
                            "Effect": "Deny",
                            "Principal": "*",
                            "Action": "s3:GetObject",
                            "Resource": "arn:aws:s3:::arquivos-privados/*"
                        }
                    ]
                }
                """.formatted(bucketName);

            s3Client.putBucketPolicy(PutBucketPolicyRequest.builder()
                    .bucket(bucketName)
                    .policy(publicReadPolicy)
                    .build());

            log.info("Bucket {} configurado como privado", bucketName);
        } catch (Exception e) {
            log.warn("Não foi possível configurar bucket {} como privado: {}", bucketName, e.getMessage());
        }
    }
}