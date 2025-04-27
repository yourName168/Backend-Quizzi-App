package com.example.gameplayservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "question_tracking")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "quiz_game_id")
    private QuizGameTracking quizGame;

    private Long userId;

    private Long questionId;

    private Boolean isCorrect;

    private LocalDateTime answeredAt;

    private Integer timeTaken;

    private Integer pointsEarned;

    private String responseChosen;
}

