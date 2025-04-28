package com.example.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for User Music and Effect Preferences")
public class UserMusicEffectDTO {

    @Schema(description = "Indicates whether music is enabled for the user", example = "true")
    private Boolean music;

    @Schema(description = "Indicates whether sound effects are enabled for the user", example = "true")
    private Boolean soundEffects;

    @Schema(description = "Indicates whether animation effects are enabled for the user", example = "false")
    private Boolean animationEffects;

    @Schema(description = "Indicates whether visual effects are enabled for the user", example = "true")
    private Boolean visualEffects;
}
