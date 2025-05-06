// Rename TypeTextOption to TextOption and make it extend Option
package com.example.questionservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "text_options")
@Data
@SuperBuilder
@AllArgsConstructor
public class TypeTextOption extends Option {

}