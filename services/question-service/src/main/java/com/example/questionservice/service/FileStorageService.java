package com.example.questionservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String baseUploadDir;

    public String storeImageFile(MultipartFile file) throws IOException {
        return storeFile(file, "image");
    }

    public String storeAudioFile(MultipartFile file) throws IOException {
        return storeFile(file, "audio");
    }

    private String storeFile(MultipartFile file, String subDir) throws IOException {
        Path uploadPath = Paths.get(baseUploadDir, subDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID() + "_" + (originalFilename != null ? originalFilename : "file");
        Path filePath = uploadPath.resolve(filename);

        Files.copy(file.getInputStream(), filePath);

        return subDir + "/" + filename;
    }

    public void deleteFile(String relativePath) throws IOException {
        Path filePath = Paths.get(baseUploadDir, relativePath);
        Files.deleteIfExists(filePath);
    }
}
