package com.example.userservice.controller;

import com.example.userservice.dto.UserMusicEffectDTO;
import com.example.userservice.model.UserMusicEffect;
import com.example.userservice.service.UserMusicEffectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-music-effects")
@RequiredArgsConstructor
public class UserMusicEffectController {
    private final UserMusicEffectService userMusicEffectService;

    @PutMapping("/{userId}")
    public ResponseEntity<UserMusicEffect> updateUserMusicEffect(@PathVariable Long userId, @RequestBody UserMusicEffectDTO dto) {
        return ResponseEntity.ok(userMusicEffectService.updateUserMusicEffect(userId, dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserMusicEffect> getUserMusicEffect(@PathVariable Long userId) {
        return ResponseEntity.ok(userMusicEffectService.getUserMusicEffect(userId));
    }
}
