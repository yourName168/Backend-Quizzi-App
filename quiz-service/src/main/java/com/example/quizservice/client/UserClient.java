package com.example.quizservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/users/{id}")
    Object getUserById(@PathVariable Long id);

    @GetMapping("/api/users/exists/{id}")
    Boolean userExists(@PathVariable Long id);
}