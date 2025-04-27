package com.example.questionservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "choice_options")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private Boolean isCorrect;
}
