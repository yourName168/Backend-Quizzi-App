package com.example.questionservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question_type_text")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class QuestionTypeText extends Question {
    @ElementCollection
    @CollectionTable(name = "accepted_answers")
    @Column(name = "answer")
    private List<String> acceptedAnswers = new ArrayList<>();

    private Boolean caseSensitive;
}
