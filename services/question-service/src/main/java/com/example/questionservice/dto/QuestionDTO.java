package com.example.questionservice.dto;

import java.time.LocalDateTime;

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
    private Long id;
    private Long quizId;
    private Long questionTypeId;
    private String image;
    private String audio;
    private String content;
    private int point;
    private int timeLimit;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}