package com.aidataagent.ai_data_analyst.dataset.service;

import com.aidataagent.ai_data_analyst.shared.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class LocalFileStorageService {

    private final Path uploadDirectory;

    public LocalFileStorageService(
            @Value("${app.storage.upload-directory}")
            String uploadDirectory) {

        try {
            this.uploadDirectory = Path.of(uploadDirectory).toAbsolutePath()
                    .normalize();

            Files.createDirectories(this.uploadDirectory);
        } catch (IOException ex) {
            throw new FileStorageException("Unable to upload directory", ex);
        }
    }

    public String store(
            MultipartFile file, String datasetId
    ) {

        try {
            String originalFileName = file.getOriginalFilename();
            String extension = getFileExtension(
                    originalFileName
            );

            String storedFileName = datasetId + "." + extension;

            Path targetPath = uploadDirectory.resolve(storedFileName);

            Files.copy(
                    file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING
            );

            return targetPath.toString();
        } catch (IOException ex) {
            throw new FileStorageException("Unable to store uploaded file", ex);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new FileStorageException("uploaded files has no extension", null);
        }
        return fileName.substring(
                fileName.lastIndexOf(".") + 1
        ).toLowerCase();
    }

}
