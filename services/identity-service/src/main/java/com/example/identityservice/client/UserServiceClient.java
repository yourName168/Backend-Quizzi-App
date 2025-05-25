package com.example.identityservice.client;

import com.example.identityservice.dto.LoginRequest;
import com.example.identityservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @PostMapping("/api/users")
    ResponseEntity<Object> createUser(@RequestBody UserDto userDto);

    @PostMapping("/api/users/authenticate")
    ResponseEntity<Object> authenticateUser(@RequestBody LoginRequest loginRequest);
}