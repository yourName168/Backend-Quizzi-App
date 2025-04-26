package com.example.gameplayservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTrackingDTO {
    private Long quizGameId;
    private Long userId;
    private Long questionId;
    private Boolean isCorrect;
    private Integer timeTaken;
    private String responseChosen;
}

