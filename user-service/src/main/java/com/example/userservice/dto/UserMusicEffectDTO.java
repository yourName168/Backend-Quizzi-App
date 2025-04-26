package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMusicEffectDTO {
    private Boolean music;
    private Boolean soundEffects;
    private Boolean animationEffects;
    private Boolean visualEffects;
}
