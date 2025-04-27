package com.example.gameplayservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "quiz-service")
public interface QuizClient {

    @GetMapping("/api/quizzes/{id}")
    Object getQuizById(@PathVariable Long id);

    @GetMapping("/api/quiz-games/{id}")
    Object getQuizGameById(@PathVariable Long id);
}

