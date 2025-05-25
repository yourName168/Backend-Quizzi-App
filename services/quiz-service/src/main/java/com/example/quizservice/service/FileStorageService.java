package com.example.quizservice.service;

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
    private String uploadDir;

    public String storeQuizCoverPhoto(MultipartFile file) throws IOException {
        return storeFile(file, "quiz");
    }

    public String storeCollectionCoverPhoto(MultipartFile file) throws IOException {
        return storeFile(file, "collection");
    }

    private String storeFile(MultipartFile file, String subDir) throws IOException {
        Path uploadPath = Paths.get(uploadDir, subDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        
        Files.copy(file.getInputStream(), filePath);
        
        return subDir + "/" + filename;
    }
    
    public void deleteFile(String relativePath) throws IOException {
        Path filePath = Paths.get(uploadDir, relativePath);
        Files.deleteIfExists(filePath);
    }
}