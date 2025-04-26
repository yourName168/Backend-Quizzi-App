package com.example.gameplayservice.controller;

import com.example.gameplayservice.dto.QuizGameTrackingDTO;
import com.example.gameplayservice.model.QuizGameTracking;
import com.example.gameplayservice.service.QuizGameTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-game-tracking")
@RequiredArgsConstructor
public class QuizGameTrackingController {
    private final QuizGameTrackingService quizGameTrackingService;

    @PostMapping
    public ResponseEntity<QuizGameTracking> createQuizGameTracking(@RequestBody QuizGameTrackingDTO dto) {
        return new ResponseEntity<>(quizGameTrackingService.createQuizGameTracking(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<QuizGameTracking>> getAllQuizGameTrackings() {
        return ResponseEntity.ok(quizGameTrackingService.getAllQuizGameTrackings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizGameTracking> getQuizGameTrackingById(@PathVariable Long id) {
        return quizGameTrackingService.getQuizGameTrackingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<QuizGameTracking>> getQuizGameTrackingsByQuizId(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizGameTrackingService.getQuizGameTrackingsByQuizId(quizId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<QuizGameTracking>> getQuizGameTrackingsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(quizGameTrackingService.getQuizGameTrackingsByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizGameTracking> updateQuizGameTracking(@PathVariable Long id, @RequestBody QuizGameTracking quizGameTracking) {
        return ResponseEntity.ok(quizGameTrackingService.updateQuizGameTracking(id, quizGameTracking));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizGameTracking(@PathVariable Long id) {
        quizGameTrackingService.deleteQuizGameTracking(id);
        return ResponseEntity.noContent().build();
    }
}