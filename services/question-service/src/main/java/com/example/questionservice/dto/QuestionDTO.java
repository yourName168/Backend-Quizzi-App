package com.example.questionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long quizId;
    private Long questionTypeId;
    private String image;
    private String audio;
    private String content;
    private Long timeLimit;
    private String description;
}