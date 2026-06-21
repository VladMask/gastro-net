package grsu.by.service.impl;

import grsu.by.config.properties.MinioProperties;
import grsu.by.service.StorageService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.SetBucketPolicyArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioStorageServiceImpl implements StorageService {

    private final MinioClient minioClient;
    private final MinioProperties properties;

    @PostConstruct
    public void ensureBucketExists() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(properties.getBucket()).build());
            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(properties.getBucket()).build());
                log.info("Created MinIO bucket: {}", properties.getBucket());
            }
        } catch (Exception e) {
            log.error("Failed to ensure MinIO bucket exists", e);
        }
        String publicPolicy = """
        {
          "Version": "2012-10-17",
          "Statement": [{
            "Effect": "Allow",
            "Principal": {"AWS": ["*"]},
            "Action": ["s3:GetObject"],
            "Resource": ["arn:aws:s3:::%s/*"]
          }]
        }
        """.formatted(properties.getBucket());

        try {
            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder()
                            .bucket(properties.getBucket())
                            .config(publicPolicy)
                            .build()
            );
        } catch (ErrorResponseException | XmlParserException | ServerException | NoSuchAlgorithmException |
                 IOException | InvalidResponseException | InvalidKeyException | InternalException |
                 InsufficientDataException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String upload(String folder, MultipartFile file) {
        String originalFilename = file.getOriginalFilename() != null
                ? file.getOriginalFilename() : "file";
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex);
        }
        String objectKey = folder + "/" + UUID.randomUUID() + extension;

        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectKey)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to upload file to MinIO: " + e.getMessage(), e);
        }

        return buildPublicUrl(objectKey);
    }

    @Override
    public void delete(String objectUrl) {
        if (objectUrl == null || objectUrl.isBlank()) {
            return;
        }
        String objectKey = extractObjectKey(objectUrl);
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectKey)
                    .build());
        } catch (Exception e) {
            log.warn("Failed to delete object from MinIO: {}", objectKey, e);
        }
    }

    private String buildPublicUrl(String objectKey) {
        return properties.getPublicEndpoint() + "/" + properties.getBucket() + "/" + objectKey;
    }


    private String extractObjectKey(String url) {
        for (String base : List.of(properties.getPublicEndpoint(), properties.getEndpoint())) {
            String prefix = base + "/" + properties.getBucket() + "/";
            if (url.startsWith(prefix)) {
                return url.substring(prefix.length());
            }
        }
        return url;
    }
}
