package com.example.gameplayservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "question-service")
public interface QuestionClient {

    @GetMapping("/api/questions/{id}")
    Object getQuestionById(@PathVariable Long id);

    @GetMapping("/api/questions/quiz/{quizId}")
    Object[] getQuestionsByQuizId(@PathVariable Long quizId);
}
