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
@Schema(description = "User Data Transfer Object")
public class UserDTO {
    @Schema(description = "Username for the user", example = "johndoe", required = true)
    private String username;
    
    @Schema(description = "Password for the user account", example = "SecurePass123!", required = true)
    private String password;
    
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;
    
    @Schema(description = "First name of the user", example = "John")
    private String fullName;
    
    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;
    
    @Schema(description = "Country of residence", example = "United States")
    private String country;
    
    @Schema(description = "Date of birth in YYYY-MM-DD format", example = "1990-01-15")
    private String dateOfBirth;
    
    @Schema(description = "Age of the user", example = "33")
    private Integer age;
    
    @Schema(description = "URL to user's avatar image", example = "https://example.com/avatars/user1.jpg")
    private String avatar;
}