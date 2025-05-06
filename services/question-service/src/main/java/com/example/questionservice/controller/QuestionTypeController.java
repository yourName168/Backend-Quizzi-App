package com.example.questionservice.controller;
import com.example.questionservice.model.QuestionType;
import com.example.questionservice.service.QuestionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuestionType(@PathVariable Long id, @RequestParam String name) {
        Optional<QuestionType> result = questionTypeService.updateQuestionType(id, name);

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Question type with ID " + id + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestionType(@PathVariable Long id) {
        boolean deleted = questionTypeService.deleteQuestionType(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Question type with ID " + id + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}