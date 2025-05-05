package com.example.userservice.service;

import com.example.userservice.dto.UserMusicEffectDTO;
import com.example.userservice.model.User;
import com.example.userservice.model.UserMusicEffect;
import com.example.userservice.repository.UserMusicEffectRepository;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMusicEffectService {
    private final UserMusicEffectRepository userMusicEffectRepository;
    private final UserRepository userRepository;

    public UserMusicEffect updateUserMusicEffect(Long userId, UserMusicEffectDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserMusicEffect musicEffect = user.getUserMusicEffect();
        musicEffect.setMusic(dto.getMusic());
        musicEffect.setSoundEffects(dto.getSoundEffects());
        musicEffect.setAnimationEffects(dto.getAnimationEffects());
        musicEffect.setVisualEffects(dto.getVisualEffects());

        return userMusicEffectRepository.save(musicEffect);
    }

    public UserMusicEffect getUserMusicEffect(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getUserMusicEffect();
    }
}
