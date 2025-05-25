package com.example.quizservice.repository;

import com.example.quizservice.model.QuizCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizCollectionRepository extends JpaRepository<QuizCollection, Long> {
    List<QuizCollection> findByAuthorId(Long authorId);
    List<QuizCollection> findByCategory(String category);
    
    @Query("SELECT qc FROM QuizCollection qc LEFT JOIN FETCH qc.quizzes WHERE qc.id = :id")
    Optional<QuizCollection> findByIdWithQuizzes(@Param("id") Long id);
    
    @Query("SELECT qc FROM QuizCollection qc LEFT JOIN FETCH qc.quizzes")
    List<QuizCollection> findAllWithQuizzes();
    
    @Query("SELECT qc FROM QuizCollection qc LEFT JOIN FETCH qc.quizzes WHERE qc.authorId = :authorId")
    List<QuizCollection> findByAuthorIdWithQuizzes(@Param("authorId") Long authorId);
    
    @Query("SELECT qc FROM QuizCollection qc LEFT JOIN FETCH qc.quizzes WHERE qc.category = :category")
    List<QuizCollection> findByCategoryWithQuizzes(@Param("category") String category);
}
