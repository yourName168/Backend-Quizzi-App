package com.example.questionservice.repository;

import com.example.questionservice.model.QuestionSlider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionSliderRepository extends JpaRepository<QuestionSlider, Long> {
}