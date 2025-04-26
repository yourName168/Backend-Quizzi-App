package com.example.gameplayservice.repository;

import com.example.gameplayservice.model.Participant;
import com.example.gameplayservice.model.QuizGameTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByQuizGame(QuizGameTracking quizGame);
    Optional<Participant> findByQuizGameAndUserId(QuizGameTracking quizGame, Long userId);
    int countByQuizGame(QuizGameTracking quizGame);
}
