package com.example.identityservice.service;

import com.example.identityservice.client.UserServiceClient;
import com.example.identityservice.dto.JwtAuthResponse;
import com.example.identityservice.dto.LoginRequest;
import com.example.identityservice.dto.RegisterRequest;
import com.example.identityservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserServiceClient userServiceClient;

    public JwtAuthResponse register(RegisterRequest registerRequest) {
        try {
            // Create user in user-service
            UserDto userDto = UserDto.builder()
                    .username(registerRequest.getUsername())
                    .email(registerRequest.getEmail())
                    .password(registerRequest.getPassword()) // user-service sẽ mã hóa password
                    .build();

            // Gọi API của user-service để tạo user
            ResponseEntity<Object> response = userServiceClient.createUser(userDto);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                // Nếu tạo thành công ở user-service, lấy thông tin user từ response
                LinkedHashMap<String, Object> userResponse = (LinkedHashMap<String, Object>) response.getBody();
                
                if (userResponse != null) {
                    // Lấy thông tin cần thiết từ response
                    Long userId = ((Number) userResponse.get("id")).longValue();
                    String username = (String) userResponse.get("username");
                    String email = (String) userResponse.get("email");

                    // Sinh JWT token
                    String token = jwtTokenProvider.generateToken(username);

                    // Trả về response với token
                    return new JwtAuthResponse(token, userId, username, email);
                }
            }

            // Nếu có lỗi từ user-service
            throw new RuntimeException("Failed to register user in user-service");
            
        } catch (Exception e) {
            log.error("Error during user registration", e);
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    public JwtAuthResponse login(LoginRequest loginRequest) {
        try {
            // Gọi API kiểm tra đăng nhập từ user-service
            ResponseEntity<Object> response = userServiceClient.authenticateUser(loginRequest);

            if (response.getStatusCode().is2xxSuccessful()) {
                LinkedHashMap<String, Object> userResponse = (LinkedHashMap<String, Object>) response.getBody();
                
                if (userResponse != null) {
                    // Lấy thông tin cần thiết từ response
                    Long userId = ((Number) userResponse.get("id")).longValue();
                    String username = (String) userResponse.get("username");
                    String email = (String) userResponse.get("email");

                    // Sinh JWT token
                    String token = jwtTokenProvider.generateToken(username);

                    // Trả về response với token
                    return new JwtAuthResponse(token, userId, username, email);
                }
            }
            
            throw new BadCredentialsException("Invalid username/email or password");
        } catch (Exception e) {
            log.error("Error during user login", e);
            throw new BadCredentialsException("Login failed: " + e.getMessage());
        }
    }
}