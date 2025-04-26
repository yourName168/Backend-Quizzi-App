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

    public QuizGameTracking createQuizGameTracking(QuizGameTrackingDTO dto) {
        try {
            Object quiz = quizClient.getQuizById(dto.getQuizId());
            if (quiz == null) {
                throw new RuntimeException("Quiz not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking quiz: " + e.getMessage());
        }

        try {
            Object user = userClient.getUserById(dto.getUserId());
            if (user == null) {
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking user: " + e.getMessage());
        }

        QuizGameTracking quizGameTracking = QuizGameTracking.builder()
                .quizId(dto.getQuizId())
                .userId(dto.getUserId())
                .totalPoints(0)
                .rank(0)
                .currentStreak(0)
                .bestStreak(0)
                .build();

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

    public QuizGameTracking updateQuizGameTracking(Long id, QuizGameTracking quizGameTracking) {
        QuizGameTracking existingQuizGameTracking = quizGameTrackingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz game tracking not found"));

        existingQuizGameTracking.setTotalPoints(quizGameTracking.getTotalPoints());
        existingQuizGameTracking.setRank(quizGameTracking.getRank());
        existingQuizGameTracking.setCurrentStreak(quizGameTracking.getCurrentStreak());
        existingQuizGameTracking.setBestStreak(quizGameTracking.getBestStreak());

        return quizGameTrackingRepository.save(existingQuizGameTracking);
    }

    public void deleteQuizGameTracking(Long id) {
        quizGameTrackingRepository.deleteById(id);
    }
}
