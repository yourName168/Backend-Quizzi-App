package com.example.questionservice.repository;

import com.example.questionservice.model.QuestionChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionChoiceRepository extends JpaRepository<QuestionChoice, Long> {
}
