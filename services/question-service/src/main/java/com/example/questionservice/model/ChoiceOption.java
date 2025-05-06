// Update ChoiceOption to extend Option
package com.example.questionservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "choice_options")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceOption extends Option {
    // No need to redefine id since it's inherited
    private Boolean isCorrect;
}