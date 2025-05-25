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
@Table(name = "quiz_collections")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long authorId;

    private String description;

    private String category;

    private Boolean visibleTo;

    private LocalDateTime timestamp;    private String coverPhoto;
    
    @OneToMany(mappedBy = "quizCollection", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private Set<Quiz> quizzes = new HashSet<>();
}