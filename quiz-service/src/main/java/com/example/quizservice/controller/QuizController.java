package com.example.quizservice.controller;

import com.example.quizservice.dto.QuizDTO;
import com.example.quizservice.model.Quiz;
import com.example.quizservice.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody QuizDTO quizDTO) {
        return new ResponseEntity<>(quizService.createQuiz(quizDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        return quizService.getQuizById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Quiz>> getQuizzesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(quizService.getQuizzesByUserId(userId));
    }

    @GetMapping("/public")
    public ResponseEntity<List<Quiz>> getPublicQuizzes() {
        return ResponseEntity.ok(quizService.getPublicQuizzes());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Quiz>> searchQuizzes(@RequestParam String title) {
        return ResponseEntity.ok(quizService.searchQuizzes(title));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long id, @RequestBody QuizDTO quizDTO) {
        return ResponseEntity.ok(quizService.updateQuiz(id, quizDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }
}
