package com.example.questionservice.controller;

import com.example.questionservice.dto.*;
import com.example.questionservice.model.*;
import com.example.questionservice.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody QuestionDTO questionDTO) {
        return new ResponseEntity<>(questionService.createQuestion(questionDTO), HttpStatus.CREATED);
    }

    @PostMapping("/true-false")
    public ResponseEntity<QuestionTrueFalse> createTrueFalseQuestion(@RequestBody QuestionTrueFalseDTO dto) {
        return new ResponseEntity<>(questionService.createTrueFalseQuestion(dto), HttpStatus.CREATED);
    }

    @PostMapping("/choice")
    public ResponseEntity<QuestionChoice> createChoiceQuestion(@RequestBody QuestionChoiceDTO dto) {
        return new ResponseEntity<>(questionService.createChoiceQuestion(dto), HttpStatus.CREATED);
    }

    @PostMapping("/slider")
    public ResponseEntity<QuestionSlider> createSliderQuestion(@RequestBody QuestionSliderDTO dto) {
        return new ResponseEntity<>(questionService.createSliderQuestion(dto), HttpStatus.CREATED);
    }

    @PostMapping("/puzzle")
    public ResponseEntity<QuestionPuzzle> createPuzzleQuestion(@RequestBody QuestionPuzzleDTO dto) {
        return new ResponseEntity<>(questionService.createPuzzleQuestion(dto), HttpStatus.CREATED);
    }

    @PostMapping("/text")
    public ResponseEntity<QuestionTypeText> createTextQuestion(@RequestBody QuestionTypeTextDTO dto) {
        return new ResponseEntity<>(questionService.createTextQuestion(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }
    
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<Question>> getQuestionsByQuizId(@PathVariable Long quizId) {
        return ResponseEntity.ok(questionService.getQuestionsByQuizId(quizId));
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Question>> getLatestQuestions() {
        List<Question> latestQuestions = questionService.getLatestQuestions();
        return ResponseEntity.ok(latestQuestions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        if (!questionService.existsById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Question with ID " + id + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable Long id, @RequestBody QuestionDTO questionDTO) {
        try {
            Question updatedQuestion = questionService.updateQuestion(id, questionDTO);
            return ResponseEntity.ok(updatedQuestion);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/true-false/{id}")
    public ResponseEntity<?> updateTrueFalseQuestion(@PathVariable Long id, @RequestBody QuestionTrueFalseDTO dto) {
        try {
            QuestionTrueFalse updatedQuestion = questionService.updateTrueFalseQuestion(id, dto);
            return ResponseEntity.ok(updatedQuestion);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/choice/{id}")
    public ResponseEntity<?> updateChoiceQuestion(@PathVariable Long id, @RequestBody QuestionChoiceDTO dto) {
        try {
            QuestionChoice updatedQuestion = questionService.updateChoiceQuestion(id, dto);
            return ResponseEntity.ok(updatedQuestion);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/slider/{id}")
    public ResponseEntity<?> updateSliderQuestion(@PathVariable Long id, @RequestBody QuestionSliderDTO dto) {
        try {
            QuestionSlider updatedQuestion = questionService.updateSliderQuestion(id, dto);
            return ResponseEntity.ok(updatedQuestion);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/puzzle/{id}")
    public ResponseEntity<?> updatePuzzleQuestion(@PathVariable Long id, @RequestBody QuestionPuzzleDTO dto) {
        try {
            QuestionPuzzle updatedQuestion = questionService.updatePuzzleQuestion(id, dto);
            return ResponseEntity.ok(updatedQuestion);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/text/{id}")
    public ResponseEntity<?> updateTextQuestion(@PathVariable Long id, @RequestBody QuestionTypeTextDTO dto) {
        try {
            QuestionTypeText updatedQuestion = questionService.updateTextQuestion(id, dto);
            return ResponseEntity.ok(updatedQuestion);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
