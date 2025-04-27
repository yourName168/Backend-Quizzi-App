package com.example.questionservice.repository;

import com.example.questionservice.model.QuestionPuzzle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionPuzzleRepository extends JpaRepository<QuestionPuzzle, Long> {
}