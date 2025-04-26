package com.example.gameplayservice.repository;

import com.example.gameplayservice.model.QuizGameTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizGameTrackingRepository extends JpaRepository<QuizGameTracking, Long> {
    List<QuizGameTracking> findByQuizId(Long quizId);
    List<QuizGameTracking> findByUserId(Long userId);
}

