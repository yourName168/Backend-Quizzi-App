package com.example.gameplayservice.controller;

import com.example.gameplayservice.dto.QuestionTrackingDTO;
import com.example.gameplayservice.model.QuestionTracking;
import com.example.gameplayservice.service.QuestionTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question-tracking")
@RequiredArgsConstructor
public class QuestionTrackingController {
    private final QuestionTrackingService questionTrackingService;

    @PostMapping
    public ResponseEntity<QuestionTracking> createQuestionTracking(@RequestBody QuestionTrackingDTO dto) {
        return new ResponseEntity<>(questionTrackingService.createQuestionTracking(dto), HttpStatus.CREATED);
    }

    @GetMapping("/quiz-game/{quizGameId}")
    public ResponseEntity<List<QuestionTracking>> getQuestionTrackingsByQuizGame(@PathVariable Long quizGameId) {
        return ResponseEntity.ok(questionTrackingService.getQuestionTrackingsByQuizGame(quizGameId));
    }

    @GetMapping("/quiz-game/{quizGameId}/user/{userId}")
    public ResponseEntity<List<QuestionTracking>> getQuestionTrackingsByQuizGameAndUser(
            @PathVariable Long quizGameId, @PathVariable Long userId) {
        return ResponseEntity.ok(questionTrackingService.getQuestionTrackingsByQuizGameAndUser(quizGameId, userId));
    }

    @GetMapping("/quiz-game/{quizGameId}/user/{userId}/question/{questionId}")
    public ResponseEntity<QuestionTracking> getQuestionTracking(
            @PathVariable Long quizGameId, @PathVariable Long userId, @PathVariable Long questionId) {
        return questionTrackingService.getQuestionTracking(quizGameId, userId, questionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

