package com.example.questionservice.controller;

import com.example.questionservice.dto.*;
import com.example.questionservice.model.*;
import com.example.questionservice.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
