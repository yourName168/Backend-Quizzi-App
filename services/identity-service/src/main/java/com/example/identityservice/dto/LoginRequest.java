package com.example.identityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    
    @NotBlank(message = "Username or email cannot be blank")
    private String usernameOrEmail;
    
    @NotBlank(message = "Password cannot be blank")
    private String password;
}