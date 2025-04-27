package com.example.questionservice.controller;

import com.example.questionservice.model.QuestionType;
import com.example.questionservice.service.QuestionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question-types")
@RequiredArgsConstructor
public class QuestionTypeController {
    private final QuestionTypeService questionTypeService;

    @PostMapping
    public ResponseEntity<QuestionType> createQuestionType(@RequestParam String name) {
        return new ResponseEntity<>(questionTypeService.createQuestionType(name), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<QuestionType>> getAllQuestionTypes() {
        return ResponseEntity.ok(questionTypeService.getAllQuestionTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionType> getQuestionTypeById(@PathVariable Long id) {
        return questionTypeService.getQuestionTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<QuestionType> getQuestionTypeByName(@PathVariable String name) {
        return questionTypeService.getQuestionTypeByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

