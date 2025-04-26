package com.example.gameplayservice.service;

import com.example.gameplayservice.client.QuestionClient;
import com.example.gameplayservice.client.UserClient;
import com.example.gameplayservice.dto.QuestionTrackingDTO;
import com.example.gameplayservice.model.QuestionTracking;
import com.example.gameplayservice.model.QuizGameTracking;
import com.example.gameplayservice.repository.QuestionTrackingRepository;
import com.example.gameplayservice.repository.QuizGameTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionTrackingService {
    private final QuestionTrackingRepository questionTrackingRepository;
    private final QuizGameTrackingRepository quizGameTrackingRepository;
    private final QuestionClient questionClient;
    private final UserClient userClient;

    public QuestionTracking createQuestionTracking(QuestionTrackingDTO dto) {
        QuizGameTracking quizGameTracking = quizGameTrackingRepository.findById(dto.getQuizGameId())
                .orElseThrow(() -> new RuntimeException("Quiz game tracking not found"));

        try {
            Object user = userClient.getUserById(dto.getUserId());
            if (user == null) {
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking user: " + e.getMessage());
        }

        try {
            Object question = questionClient.getQuestionById(dto.getQuestionId());
            if (question == null) {
                throw new RuntimeException("Question not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking question: " + e.getMessage());
        }

        int pointsEarned = calculatePoints(dto.getIsCorrect(), dto.getTimeTaken());

        QuestionTracking questionTracking = QuestionTracking.builder()
                .quizGame(quizGameTracking)
                .userId(dto.getUserId())
                .questionId(dto.getQuestionId())
                .isCorrect(dto.getIsCorrect())
                .answeredAt(LocalDateTime.now())
                .timeTaken(dto.getTimeTaken())
                .pointsEarned(pointsEarned)
                .responseChosen(dto.getResponseChosen())
                .build();

        updateQuizGameTrackingStats(quizGameTracking, dto.getIsCorrect(), pointsEarned);

        return questionTrackingRepository.save(questionTracking);
    }

    public List<QuestionTracking> getQuestionTrackingsByQuizGame(Long quizGameId) {
        QuizGameTracking quizGameTracking = quizGameTrackingRepository.findById(quizGameId)
                .orElseThrow(() -> new RuntimeException("Quiz game tracking not found"));

        return questionTrackingRepository.findByQuizGame(quizGameTracking);
    }

    public List<QuestionTracking> getQuestionTrackingsByQuizGameAndUser(Long quizGameId, Long userId) {
        QuizGameTracking quizGameTracking = quizGameTrackingRepository.findById(quizGameId)
                .orElseThrow(() -> new RuntimeException("Quiz game tracking not found"));

        return questionTrackingRepository.findByQuizGameAndUserId(quizGameTracking, userId);
    }

    public Optional<QuestionTracking> getQuestionTracking(Long quizGameId, Long userId, Long questionId) {
        QuizGameTracking quizGameTracking = quizGameTrackingRepository.findById(quizGameId)
                .orElseThrow(() -> new RuntimeException("Quiz game tracking not found"));

        return questionTrackingRepository.findByQuizGameAndUserIdAndQuestionId(quizGameTracking, userId, questionId);
    }

    private int calculatePoints(boolean isCorrect, int timeTaken) {
        if (!isCorrect) {
            return 0;
        }

        int basePoints = 100;

        int timeBonus = Math.max(0, 60 - timeTaken) * 5;

        return basePoints + timeBonus;
    }

    private void updateQuizGameTrackingStats(QuizGameTracking quizGameTracking, boolean isCorrect, int pointsEarned) {
        quizGameTracking.setTotalPoints(quizGameTracking.getTotalPoints() + pointsEarned);

        if (isCorrect) {
            quizGameTracking.setCurrentStreak(quizGameTracking.getCurrentStreak() + 1);
            if (quizGameTracking.getCurrentStreak() > quizGameTracking.getBestStreak()) {
                quizGameTracking.setBestStreak(quizGameTracking.getCurrentStreak());
            }
        } else {
            quizGameTracking.setCurrentStreak(0);
        }

        quizGameTrackingRepository.save(quizGameTracking);
    }
}