package com.example.quizservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizCollectionDTO {
    private Long authorId;
    private Long quizId;
    private String description;
    private String category;
    private Boolean visibleTo;
}