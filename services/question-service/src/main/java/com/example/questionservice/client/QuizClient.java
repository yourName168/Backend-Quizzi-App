package com.example.questionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "quiz-service")
public interface QuizClient {

    @GetMapping("/api/quizzes/{id}")
    Object getQuizById(@PathVariable("id") Long id);

    @GetMapping("/api/quizzes/exists/{id}")
    Boolean quizExists(@PathVariable("id") Long id);
}
