package com.example.quizservice.controller;

import com.example.quizservice.dto.QuizCollectionDTO;
import com.example.quizservice.model.QuizCollection;
import com.example.quizservice.service.QuizCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-collections")
@RequiredArgsConstructor
public class QuizCollectionController {
    private final QuizCollectionService quizCollectionService;

    @PostMapping
    public ResponseEntity<QuizCollection> createQuizCollection(@RequestBody QuizCollectionDTO dto) {
        return new ResponseEntity<>(quizCollectionService.createQuizCollection(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<QuizCollection>> getAllQuizCollections() {
        return ResponseEntity.ok(quizCollectionService.getAllQuizCollections());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizCollection> getQuizCollectionById(@PathVariable Long id) {
        return quizCollectionService.getQuizCollectionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<QuizCollection>> getQuizCollectionsByAuthorId(@PathVariable Long authorId) {
        return ResponseEntity.ok(quizCollectionService.getQuizCollectionsByAuthorId(authorId));
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<QuizCollection>> getQuizCollectionsByQuizId(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizCollectionService.getQuizCollectionsByQuizId(quizId));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<QuizCollection>> getQuizCollectionsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(quizCollectionService.getQuizCollectionsByCategory(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizCollection> updateQuizCollection(@PathVariable Long id, @RequestBody QuizCollectionDTO dto) {
        return ResponseEntity.ok(quizCollectionService.updateQuizCollection(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizCollection(@PathVariable Long id) {
        quizCollectionService.deleteQuizCollection(id);
        return ResponseEntity.noContent().build();
    }
}
