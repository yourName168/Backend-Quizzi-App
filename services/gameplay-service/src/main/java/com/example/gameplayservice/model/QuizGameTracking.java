package com.example.gameplayservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "quiz_game_tracking")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizGameTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quizId;

    private Long userId;

    private Integer totalPoints;

    @Column(name = "`rank`")
    private Integer rank;

    private Integer currentStreak;

    private Integer bestStreak;

}
