package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.model.User;
import com.example.userservice.model.UserMusicEffect;
import com.example.userservice.repository.UserMusicEffectRepository;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMusicEffectRepository userMusicEffectRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    public User createUser(UserDTO userDTO, MultipartFile avatarFile) throws IOException {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        UserMusicEffect userMusicEffect = UserMusicEffect.builder()
                .music(false)
                .soundEffects(false)
                .animationEffects(false)
                .visualEffects(false)
                .build();

        userMusicEffectRepository.save(userMusicEffect);

        User user = User.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .email(userDTO.getEmail())
                .createdAt(LocalDateTime.now())
                .fullName(userDTO.getFullName())
                .lastName(userDTO.getLastName())
                .country(userDTO.getCountry())
                .dateOfBirth(userDTO.getDateOfBirth())
                .age(userDTO.getAge())
                .totalQuizs(0)
                .totalCollections(0)
                .totalPlays(0)
                .totalFollowers(0)
                .totalFollowees(0)
                .totalPlayers(0)
                .userMusicEffect(userMusicEffect)
                .build();

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarPath = fileStorageService.storeUserAvatar(avatarFile);
            user.setAvatar(avatarPath);
        } else if (userDTO.getAvatar() != null) {
            user.setAvatar(userDTO.getAvatar());
        }

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(Long id, UserDTO userDTO, MultipartFile avatarFile) throws IOException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setLastName(userDTO.getLastName());
        user.setCountry(userDTO.getCountry());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setAge(userDTO.getAge());

        if (avatarFile != null && !avatarFile.isEmpty()) {
            if (user.getAvatar() != null) {
                fileStorageService.deleteFile(user.getAvatar());
            }
            
            String avatarPath = fileStorageService.storeUserAvatar(avatarFile);
            user.setAvatar(avatarPath);
        } else if (userDTO.getAvatar() != null && !userDTO.getAvatar().equals(user.getAvatar())) {
            user.setAvatar(userDTO.getAvatar());
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id) throws IOException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getAvatar() != null) {
            fileStorageService.deleteFile(user.getAvatar());
        }

        userRepository.deleteById(id);
    }
    
    public Optional<User> authenticateUser(String usernameOrEmail, String password) {
        Optional<User> userOptional = userRepository.findByUsername(usernameOrEmail);
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByEmail(usernameOrEmail);
        }
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return userOptional;
            }
        }
        
        return Optional.empty();
    }
}