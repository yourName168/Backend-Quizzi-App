package com.example.questionservice.repository;

import com.example.questionservice.model.QuestionTypeText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTypeTextRepository extends JpaRepository<QuestionTypeText, Long> {
}