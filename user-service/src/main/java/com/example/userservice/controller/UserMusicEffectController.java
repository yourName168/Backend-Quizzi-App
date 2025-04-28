package com.example.userservice.controller;

import com.example.userservice.dto.UserMusicEffectDTO;
import com.example.userservice.model.UserMusicEffect;
import com.example.userservice.service.UserMusicEffectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-music-effects")
@RequiredArgsConstructor
@Tag(name = "User Music Effects", description = "APIs for managing user music effects")
public class UserMusicEffectController {
    private final UserMusicEffectService userMusicEffectService;

    @PutMapping("/{userId}")
    @Operation(summary = "Update user music effect", description = "Updates or creates music effect settings for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Music effect updated successfully",
                content = @Content(schema = @Schema(implementation = UserMusicEffect.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid music effect data")
    })
    public ResponseEntity<UserMusicEffect> updateUserMusicEffect(@PathVariable @Parameter(description = "User ID") Long userId, 
                                                                 @RequestBody @Parameter(description = "Music effect settings") UserMusicEffectDTO dto) {
        return ResponseEntity.ok(userMusicEffectService.updateUserMusicEffect(userId, dto));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user music effect", description = "Retrieves music effect settings for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Music effect retrieved successfully",
                content = @Content(schema = @Schema(implementation = UserMusicEffect.class))),
        @ApiResponse(responseCode = "404", description = "User or music effect not found")
    })
    public ResponseEntity<UserMusicEffect> getUserMusicEffect(@PathVariable @Parameter(description = "User ID") Long userId) {
        return ResponseEntity.ok(userMusicEffectService.getUserMusicEffect(userId));
    }
}