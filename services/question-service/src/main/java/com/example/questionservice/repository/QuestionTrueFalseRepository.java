package com.example.questionservice.repository;

import com.example.questionservice.model.QuestionTrueFalse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTrueFalseRepository extends JpaRepository<QuestionTrueFalse, Long> {
}

