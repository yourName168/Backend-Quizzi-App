package com.example.quizservice.service;

import com.example.quizservice.client.UserClient;
import com.example.quizservice.dto.QuizDTO;
import com.example.quizservice.model.Quiz;
import com.example.quizservice.model.QuizCollection;
import com.example.quizservice.repository.QuizCollectionRepository;
import com.example.quizservice.repository.QuizRepository;
import lombok.RequiredArgsConstructor;

import org.hibernate.annotations.CollectionId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizCollectionRepository quizCollectionRepository;
    private final UserClient userClient;
    private final FileStorageService fileStorageService;

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    public List<Quiz> getLatestQuizzes() {
        return quizRepository.findTop10ByOrderByCreatedAtDesc();
    }

    public List<Quiz> getQuizzesByUserId(Long userId) {
        return quizRepository.findByUserId(userId);
    }

    public List<Quiz> getPublicQuizzes() {
        return quizRepository.findByVisibleTrue();
    }

    public List<Quiz> searchQuizzes(String title) {
        return quizRepository.findByTitleContainingIgnoreCase(title);
    }

    public Quiz createQuiz(QuizDTO quizDTO, MultipartFile coverPhoto) throws IOException {
        Quiz quiz = convertToEntity(quizDTO);

        if (coverPhoto != null && !coverPhoto.isEmpty()) {
            String photoPath = fileStorageService.storeQuizCoverPhoto(coverPhoto);
            quiz.setCoverPhoto(photoPath);
        } else if (quizDTO.getCoverPhoto() != null) {
            quiz.setCoverPhoto(quizDTO.getCoverPhoto());
        }

        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setUpdatedAt(LocalDateTime.now());

        return quizRepository.save(quiz);
    }

    public Quiz updateQuiz(Long id, QuizDTO quizDTO, MultipartFile coverPhoto) throws IOException {
        Quiz existingQuiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));

        existingQuiz.setTitle(quizDTO.getTitle());
        existingQuiz.setDescription(quizDTO.getDescription());
        existingQuiz.setKeyword(quizDTO.getKeyword());
        existingQuiz.setVisible(quizDTO.getVisible());
        existingQuiz.setVisibleQuizQuestion(quizDTO.getVisibleQuizQuestion());
        existingQuiz.setShuffle(quizDTO.getShuffle());

        if (quizDTO.getQuizCollectionId() != null) {
            QuizCollection quizCollection = quizCollectionRepository
                    .findById(Long.parseLong(quizDTO.getQuizCollectionId()))
                    .orElseThrow(() -> new RuntimeException(
                            "Quiz Collection not found with id: " + quizDTO.getQuizCollectionId()));
            existingQuiz.setQuizCollection(quizCollection);
        }

        if (coverPhoto != null && !coverPhoto.isEmpty()) {
            if (existingQuiz.getCoverPhoto() != null) {
                fileStorageService.deleteFile(existingQuiz.getCoverPhoto());
            }

            String photoPath = fileStorageService.storeQuizCoverPhoto(coverPhoto);
            existingQuiz.setCoverPhoto(photoPath);
        } else if (quizDTO.getCoverPhoto() != null && !quizDTO.getCoverPhoto().equals(existingQuiz.getCoverPhoto())) {
            existingQuiz.setCoverPhoto(quizDTO.getCoverPhoto());
        }

        existingQuiz.setUpdatedAt(LocalDateTime.now());

        return quizRepository.save(existingQuiz);
    }

    public void deleteQuiz(Long id) throws IOException {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));

        if (quiz.getCoverPhoto() != null) {
            fileStorageService.deleteFile(quiz.getCoverPhoto());
        }

        quizRepository.delete(quiz);
    }

    private Quiz convertToEntity(QuizDTO quizDTO) {
        Quiz quiz = new Quiz();
        quiz.setUserId(quizDTO.getUserId());
        quiz.setTitle(quizDTO.getTitle());
        quiz.setDescription(quizDTO.getDescription());
        quiz.setKeyword(quizDTO.getKeyword());
        quiz.setVisible(quizDTO.getVisible());
        quiz.setVisibleQuizQuestion(quizDTO.getVisibleQuizQuestion());
        quiz.setShuffle(quizDTO.getShuffle());

        if (quizDTO.getQuizCollectionId() != null && !quizDTO.getQuizCollectionId().isEmpty()) {
            try {
                Long collectionId = Long.parseLong(quizDTO.getQuizCollectionId());
                QuizCollection quizCollection = quizCollectionRepository.findById(collectionId)
                        .orElseThrow(() -> new RuntimeException(
                                "Quiz Collection not found with id: " + quizDTO.getQuizCollectionId()));
                quiz.setQuizCollection(quizCollection);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid quiz collection ID: " + quizDTO.getQuizCollectionId());
            }
        }

        return quiz;
    }

    public Set<Quiz> getQuizzesByCollection(QuizCollection collection) {
        return quizRepository.findByQuizCollection(collection);
    }

    public Set<Quiz> getQuizzesByCollectionId(Long id) {
        return quizRepository.findByQuizCollectionId(id);
    }
    
}