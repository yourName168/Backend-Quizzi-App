package com.example.quizservice.controller;

import com.example.quizservice.dto.QuizGameDTO;
import com.example.quizservice.model.QuizGame;
import com.example.quizservice.service.QuizGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-games")
@RequiredArgsConstructor
public class QuizGameController {
    private final QuizGameService quizGameService;

    @PostMapping
    public ResponseEntity<QuizGame> createQuizGame(@RequestBody QuizGameDTO quizGameDTO) {
        return new ResponseEntity<>(quizGameService.createQuizGame(quizGameDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<QuizGame>> getAllQuizGames() {
        return ResponseEntity.ok(quizGameService.getAllQuizGames());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizGame> getQuizGameById(@PathVariable Long id) {
        return quizGameService.getQuizGameById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<QuizGame>> getQuizGamesByQuizId(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizGameService.getQuizGamesByQuizId(quizId));
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<QuizGame>> getQuizGamesByAdminId(@PathVariable Long adminId) {
        return ResponseEntity.ok(quizGameService.getQuizGamesByAdminId(adminId));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<QuizGame> getQuizGameByCode(@PathVariable String code) {
        return quizGameService.getQuizGameByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizGame(@PathVariable Long id) {
        quizGameService.deleteQuizGame(id);
        return ResponseEntity.noContent().build();
    }
}
