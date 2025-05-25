package com.example.questionservice.repository;

import com.example.questionservice.dto.QuestionDTO;
import com.example.questionservice.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAll();
    List<Question> findByQuizId(Long quizId);
    List<Question> findTop10ByOrderByCreatedAtDesc();
    boolean existsByQuizId(Long id);
    List<Question> findByQuizIdOrderByCreatedAtAsc(Long quizId);
    List<Question> findByQuizIdOrderByPositionAsc(Long quizId);
}

