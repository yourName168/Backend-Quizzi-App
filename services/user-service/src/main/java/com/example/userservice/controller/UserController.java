package com.example.userservice.controller;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import com.example.userservice.service.UrlService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UrlService urlService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> createUser(@ModelAttribute UserDTO userDTO, HttpServletRequest request) {
        try {
            User createdUser = userService.createUser(userDTO, userDTO.getAvatarFile());
            enrichWithCompleteUrl(createdUser, request);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(HttpServletRequest request) {
        List<User> users = userService.getAllUsers();
        users.forEach(user -> enrichWithCompleteUrl(user, request));
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id, HttpServletRequest request) {
        return userService.getUserById(id)
                .map(user -> {
                    enrichWithCompleteUrl(user, request);
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username, HttpServletRequest request) {
        return userService.getUserByUsername(username)
                .map(user -> {
                    enrichWithCompleteUrl(user, request);
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> updateUser(
            @PathVariable Long id, 
            @ModelAttribute UserDTO userDTO,
            HttpServletRequest request) {
        try {
            User updatedUser = userService.updateUser(id, userDTO, userDTO.getAvatarFile());
            enrichWithCompleteUrl(updatedUser, request);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/authenticate")
    public ResponseEntity<User> authenticateUser(@RequestBody Map<String, String> credentials, HttpServletRequest request) {
        String usernameOrEmail = credentials.get("usernameOrEmail");
        String password = credentials.get("password");
        
        return userService.authenticateUser(usernameOrEmail, password)
                .map(user -> {
                    enrichWithCompleteUrl(user, request);
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    private void enrichWithCompleteUrl(User user, HttpServletRequest request) {
        if (user.getAvatar() != null) {
            user.setAvatar(urlService.getCompleteFileUrl(user.getAvatar(), request));
        }
    }
}