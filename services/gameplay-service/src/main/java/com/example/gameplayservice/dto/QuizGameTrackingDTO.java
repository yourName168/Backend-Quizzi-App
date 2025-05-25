package com.example.gameplayservice.dto;

import java.util.HashSet;
import java.util.Set;

import com.example.gameplayservice.model.Participant;
import com.example.gameplayservice.model.QuestionTracking;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizGameTrackingDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quizId;

    private Long userId;

    @OneToMany(mappedBy = "quizGame", cascade = CascadeType.ALL)
    private Set<Participant> participants = new HashSet<>();

    private Integer totalPoints;

    @Column(name = "`rank`")
    private Integer rank;

    private Integer currentStreak;

    private Integer bestStreak;

    @OneToMany(mappedBy = "quizGame", cascade = CascadeType.ALL)
    private Set<QuestionTracking> questionTrackings = new HashSet<>();
}
