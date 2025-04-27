package com.example.quizservice.service;

import com.example.quizservice.client.UserClient;
import com.example.quizservice.dto.QuizDTO;
import com.example.quizservice.model.Quiz;
import com.example.quizservice.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final UserClient userClient;

    public Quiz createQuiz(QuizDTO quizDTO) {
        // Check if user exists
        try {
            Object user = userClient.getUserById(quizDTO.getUserId());
            if (user == null) {
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking user: " + e.getMessage());
        }

        Quiz quiz = Quiz.builder()
                .userId(quizDTO.getUserId())
                .title(quizDTO.getTitle())
                .description(quizDTO.getDescription())
                .keyword(quizDTO.getKeyword())
                .score(0)
                .coverPhoto(quizDTO.getCoverPhoto())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .visible(quizDTO.getVisible())
                .visibleQuizQuestion(quizDTO.getVisibleQuizQuestion())
                .shuffle(quizDTO.getShuffle())
                .build();

        return quizRepository.save(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
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

    public Quiz updateQuiz(Long id, QuizDTO quizDTO) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        quiz.setTitle(quizDTO.getTitle());
        quiz.setDescription(quizDTO.getDescription());
        quiz.setKeyword(quizDTO.getKeyword());
        quiz.setCoverPhoto(quizDTO.getCoverPhoto());
        quiz.setVisible(quizDTO.getVisible());
        quiz.setVisibleQuizQuestion(quizDTO.getVisibleQuizQuestion());
        quiz.setShuffle(quizDTO.getShuffle());
        quiz.setUpdatedAt(LocalDateTime.now());

        return quizRepository.save(quiz);
    }

    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }
}
