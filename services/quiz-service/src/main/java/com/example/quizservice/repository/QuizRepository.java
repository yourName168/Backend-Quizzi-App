package com.example.quizservice.repository;


import com.example.quizservice.model.Quiz;
import com.example.quizservice.model.QuizCollection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByUserId(Long userId);
    List<Quiz> findByVisibleTrue();
    List<Quiz> findByTitleContainingIgnoreCase(String title);
    List<Quiz> findTop10ByOrderByCreatedAtDesc();

    
    Set<Quiz> findByQuizCollection(QuizCollection quizCollection);
    Set<Quiz> findByQuizCollectionId(Long id);
}