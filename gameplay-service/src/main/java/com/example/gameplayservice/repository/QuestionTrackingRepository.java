package com.example.gameplayservice.repository;

import com.example.gameplayservice.model.QuestionTracking;
import com.example.gameplayservice.model.QuizGameTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionTrackingRepository extends JpaRepository<QuestionTracking, Long> {
    List<QuestionTracking> findByQuizGame(QuizGameTracking quizGame);
    List<QuestionTracking> findByQuizGameAndUserId(QuizGameTracking quizGame, Long userId);
    Optional<QuestionTracking> findByQuizGameAndUserIdAndQuestionId(QuizGameTracking quizGame, Long userId, Long questionId);
}