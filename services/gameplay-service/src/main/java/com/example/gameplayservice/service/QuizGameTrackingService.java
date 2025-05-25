package com.example.gameplayservice.service;

import com.example.gameplayservice.client.QuizClient;
import com.example.gameplayservice.client.UserClient;
import com.example.gameplayservice.dto.QuizGameTrackingDTO;
import com.example.gameplayservice.model.QuizGameTracking;
import com.example.gameplayservice.repository.QuizGameTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizGameTrackingService {
    private final QuizGameTrackingRepository quizGameTrackingRepository;
    private final QuizClient quizClient;
    private final UserClient userClient;

    public QuizGameTracking createQuizGameTracking(QuizGameTracking quizGameTracking) {
        try {
            Object quiz = quizClient.getQuizById(quizGameTracking.getQuizId());
            if (quiz == null) {
                throw new RuntimeException("Quiz not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking quiz: " + e.getMessage());
        }

        try {
            Object user = userClient.getUserById(quizGameTracking.getUserId());
            if (user == null) {
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking user: " + e.getMessage());
        }

        return quizGameTrackingRepository.save(quizGameTracking);
    }

    public List<QuizGameTracking> getAllQuizGameTrackings() {
        return quizGameTrackingRepository.findAll();
    }

    public Optional<QuizGameTracking> getQuizGameTrackingById(Long id) {
        return quizGameTrackingRepository.findById(id);
    }

    public List<QuizGameTracking> getQuizGameTrackingsByQuizId(Long quizId) {
        return quizGameTrackingRepository.findByQuizId(quizId);
    }

    public List<QuizGameTracking> getQuizGameTrackingsByUserId(Long userId) {
        return quizGameTrackingRepository.findByUserId(userId);
    }

    public void deleteQuizGameTracking(Long id) {
        quizGameTrackingRepository.deleteById(id);
    }
}
