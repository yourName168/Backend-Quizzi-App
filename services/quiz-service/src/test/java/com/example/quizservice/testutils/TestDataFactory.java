package com.example.quizservice.testutils;

import com.example.quizservice.dto.QuizCollectionDTO;
import com.example.quizservice.dto.QuizDTO;
import com.example.quizservice.model.Quiz;
import com.example.quizservice.model.QuizCollection;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;

public class TestDataFactory {
   
    public static QuizCollectionDTO createQuizCollectionDTO() {
        return QuizCollectionDTO.builder()
                .authorId(1L)
                .description("Bộ sưu tập bài quiz mẫu")
                .category("Toán học")
                .visibleTo(true)
                .coverPhoto(null)
                .build();
    }

    public static QuizCollectionDTO createQuizCollectionDTOWithImage() throws IOException {
        QuizCollectionDTO dto = createQuizCollectionDTO();
        dto.setCoverPhotoFile(createMockImageFile());
        return dto;
    }

    public static QuizCollectionDTO createQuizCollectionDTOForUpdate(Long id) {
        QuizCollectionDTO dto = createQuizCollectionDTO();
        dto.setId(id);
        dto.setDescription("Bộ sưu tập bài quiz đã cập nhật");
        return dto;
    }

    public static QuizCollectionDTO createQuizCollectionDTOForUpdateWithImage(Long id) throws IOException {
        QuizCollectionDTO dto = createQuizCollectionDTOForUpdate(id);
        dto.setCoverPhotoFile(createMockImageFile());
        return dto;
    }

    public static MultipartFile createMockImageFile() throws IOException {
        Path imagePath = Paths.get("src/test/resources/test-files/images/test-image.jpg");
        
        if (!Files.exists(imagePath)) {
            Files.createDirectories(imagePath.getParent());
            Files.createFile(imagePath);
        }
        
        byte[] content;
        try {
            content = Files.readAllBytes(imagePath);
        } catch (IOException e) {
            content = new byte[100];
            for (int i = 0; i < content.length; i++) {
                content[i] = (byte) i;
            }
        }
        
        return new MockMultipartFile(
                "coverPhotoFile",
                "test-image.jpg",
                "image/jpeg",
                content
        );
    }


    public static QuizDTO createQuizDTO() {
        return QuizDTO.builder()
                .userId(1L)
                .title("Bài quiz mẫu")
                .description("Mô tả bài quiz mẫu")
                .keyword("toán,học,mẫu")
                .visible(true)
                .visibleQuizQuestion(true)
                .shuffle(false)
                .build();
    }

    public static QuizDTO createQuizDTOWithCollection(Long collectionId) {
        QuizDTO dto = createQuizDTO();
        dto.setQuizCollectionId(String.valueOf(collectionId));
        return dto;
    }

    public static QuizDTO createQuizDTOWithImage() throws IOException {
        QuizDTO dto = createQuizDTO();
        dto.setCoverPhotoFile(createMockImageFile());
        return dto;
    }

    public static QuizDTO createQuizDTOWithImageAndCollection(Long collectionId) throws IOException {
        QuizDTO dto = createQuizDTOWithCollection(collectionId);
        dto.setCoverPhotoFile(createMockImageFile());
        return dto;
    }

    public static QuizDTO createQuizDTOForUpdate(Long id) {
        QuizDTO dto = createQuizDTO();
        dto.setTitle("Bài quiz đã cập nhật");
        dto.setDescription("Mô tả bài quiz đã cập nhật");
        return dto;
    }

    public static QuizDTO createQuizDTOForUpdateWithImage(Long id) throws IOException {
        QuizDTO dto = createQuizDTOForUpdate(id);
        dto.setCoverPhotoFile(createMockImageFile());
        return dto;
    }


    public static QuizCollection createMockQuizCollection(Long id) {
        return QuizCollection.builder()
                .id(id)
                .authorId(1L)
                .description("Bộ sưu tập bài quiz mẫu")
                .category("Toán học")
                .visibleTo(true)
                .coverPhoto("quiz-collections/1/cover.jpg")
                .quizzes(Collections.emptySet())
                .build();
    }

    public static Quiz createMockQuiz(Long id) {
        return Quiz.builder()
                .id(id)
                .userId(1L)
                .title("Bài quiz " + id)
                .description("Mô tả bài quiz " + id)
                .keyword("toán,học,mẫu")
                .score(0)
                .coverPhoto("quizzes/" + id + "/cover.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .visible(true)
                .visibleQuizQuestion(true)
                .shuffle(false)
                .build();
    }


    public static Quiz createMockQuizWithCollection(Long id, QuizCollection collection) {
        Quiz quiz = createMockQuiz(id);
        quiz.setQuizCollection(collection);
        return quiz;
    }
}