package com.example.quizservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    @ManyToOne
    @JoinColumn(name = "collection_id")
    @JsonIgnore
    private QuizCollection quizCollection;
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonManagedReference
    private Set<QuizGame> quizGames;

}