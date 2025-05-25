package com.example.userservice.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String lastName;
    private String country;
    private String dateOfBirth;
    private Integer age;
    private String avatar;

    private transient MultipartFile avatarFile;
}

