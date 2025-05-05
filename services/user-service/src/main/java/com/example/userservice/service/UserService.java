package com.example.userservice.service;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.model.User;
import com.example.userservice.model.UserMusicEffect;
import com.example.userservice.repository.UserMusicEffectRepository;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMusicEffectRepository userMusicEffectRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(UserDTO userDTO) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create user music effect
        UserMusicEffect userMusicEffect = UserMusicEffect.builder()
                .music(false)
                .soundEffects(false)
                .animationEffects(false)
                .visualEffects(false)
                .build();

        userMusicEffectRepository.save(userMusicEffect);

        // Create user
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
                .avatar(userDTO.getAvatar())
                .totalQuizs(0)
                .totalCollections(0)
                .totalPlays(0)
                .totalFollowers(0)
                .totalFollowees(0)
                .totalPlayers(0)
                .userMusicEffect(userMusicEffect)
                .build();

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

    public User updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setLastName(userDTO.getLastName());
        user.setCountry(userDTO.getCountry());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setAge(userDTO.getAge());
        user.setAvatar(userDTO.getAvatar());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    public Optional<User> authenticateUser(String usernameOrEmail, String password) {
        // Tìm user theo username hoặc email
        Optional<User> userOptional = userRepository.findByUsername(usernameOrEmail);
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByEmail(usernameOrEmail);
        }
        
        // Kiểm tra nếu user tồn tại và mật khẩu hợp lệ
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return userOptional;
            }
        }
        
        return Optional.empty();
    }
}