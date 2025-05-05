package com.example.quizservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "quizzes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String title;

    private String description;

    private String keyword;

    private Integer score;

    private String coverPhoto;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean visible;

    private Boolean visibleQuizQuestion;

    private Boolean shuffle;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private Set<QuizGame> quizGames = new HashSet<>();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private Set<QuizCollection> quizCollections = new HashSet<>();
}
