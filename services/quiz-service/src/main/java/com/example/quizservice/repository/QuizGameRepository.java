package com.example.quizservice.repository;

import com.example.quizservice.model.QuizGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizGameRepository extends JpaRepository<QuizGame, Long> {
    List<QuizGame> findByQuizId(Long quizId);
    List<QuizGame> findByAdminId(Long adminId);
    Optional<QuizGame> findByCode(String code);
}
