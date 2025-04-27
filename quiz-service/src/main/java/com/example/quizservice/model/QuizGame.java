package com.example.quizservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_games")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long adminId;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    private LocalDateTime startedAt;

    private String code;

    private String qrCode;

    private Integer totalParticipants;
}
