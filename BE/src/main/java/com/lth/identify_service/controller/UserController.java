package com.lth.identify_service.controller;

import com.lth.identify_service.dto.request.UserCreationRequest;
import com.lth.identify_service.dto.request.UserUpdateRequest;
import com.lth.identify_service.dto.response.ApiResponse;
import com.lth.identify_service.dto.response.UserResponse;
import com.lth.identify_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse response = new ApiResponse(201, "User created successfully", userService.createUser(request));

        return response;
    }

    @GetMapping
    ApiResponse<List> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("User: {}", authentication.getPrincipal());
        authentication.getAuthorities().forEach(authority -> log.info("Authority: {}", authority.getAuthority()));
        ApiResponse response = new ApiResponse(200, "Users retrieved successfully", userService.getUsers());
        return response;
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        ApiResponse response = new ApiResponse(200, "User retrieved successfully", userService.getUser(userId));
        return response;
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        ApiResponse response = new ApiResponse(200, "User updated successfully", userService.updateUser(userId, request));
        return response;
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable("userId") String userId) {
        ApiResponse response = new ApiResponse(200, "User deleted successfully", userService.deleteUser(userId));
        return response;
    }

    @GetMapping("getMyInfor")
    ApiResponse<UserResponse> getMyInfor() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        ApiResponse response = new ApiResponse(200, "User retrieved successfully", userService.getUser(userId));
        return response;
    }

}
