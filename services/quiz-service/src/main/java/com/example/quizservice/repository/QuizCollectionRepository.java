package com.example.quizservice.repository;

import com.example.quizservice.model.QuizCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizCollectionRepository extends JpaRepository<QuizCollection, Long> {
    List<QuizCollection> findByAuthorId(Long authorId);
    List<QuizCollection> findByQuizId(Long quizId);
    List<QuizCollection> findByCategory(String category);
}
