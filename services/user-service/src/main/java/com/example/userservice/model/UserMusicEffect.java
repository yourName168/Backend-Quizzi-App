package com.example.userservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_music_effects")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMusicEffect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean music;

    private Boolean soundEffects;

    private Boolean animationEffects;

    private Boolean visualEffects;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_music_effect_id")
    private UserMusicEffect userMusicEffect;
}