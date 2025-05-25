package com.example.questionservice.controller;

import com.example.questionservice.dto.*;
import com.example.questionservice.model.*;
import com.example.questionservice.service.QuestionService;
import com.example.questionservice.service.UrlService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final UrlService urlService;

    @PostMapping(value = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createQuestionsBatch(
            @RequestParam("quizId") Long quizId,
            @RequestParam("questionsJson") String questionsJson,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            HttpServletRequest request) {
        
        try {
            List<Question> createdQuestions = questionService.createQuestionsBatch(quizId, questionsJson, files);
            
            // Enrich all questions with complete URLs
            createdQuestions.forEach(q -> enrichWithCompleteUrl(q, request));
            
            return ResponseEntity.ok(createdQuestions);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Batch question creation failed: " + e.getMessage());
            response.put("details", e.toString());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createQuestion(
            @ModelAttribute QuestionDTO questionDTO,
            HttpServletRequest request) throws IOException {
        try {
            Question question = questionService.createQuestion(questionDTO);
            enrichWithCompleteUrl(question, request);
            return ResponseEntity.ok(question);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Question creation failed: " + e.getMessage());
            response.put("details", e.toString());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(value = "/true-false", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<QuestionTrueFalse> createTrueFalseQuestion(
            @ModelAttribute QuestionTrueFalseDTO dto,
            HttpServletRequest request) throws IOException {
        QuestionTrueFalse question = questionService.createTrueFalseQuestion(dto);
        enrichWithCompleteUrl(question, request);
        return new ResponseEntity<>(question, HttpStatus.CREATED);
    }

    @PostMapping(value = "/choice", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<QuestionChoice> createChoiceQuestion(
            @ModelAttribute QuestionChoiceDTO dto,
            HttpServletRequest request) throws IOException {
        QuestionChoice question = questionService.createChoiceQuestion(dto);
        enrichWithCompleteUrl(question, request);
        return new ResponseEntity<>(question, HttpStatus.CREATED);
    }

    @PostMapping(value = "/slider", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<QuestionSlider> createSliderQuestion(
            @ModelAttribute QuestionSliderDTO dto,
            HttpServletRequest request) throws IOException {
        QuestionSlider question = questionService.createSliderQuestion(dto);
        enrichWithCompleteUrl(question, request);
        return new ResponseEntity<>(question, HttpStatus.CREATED);
    }

    @PostMapping(value = "/puzzle", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<QuestionPuzzle> createPuzzleQuestion(
            @ModelAttribute QuestionPuzzleDTO dto,
            HttpServletRequest request) throws IOException {
        QuestionPuzzle question = questionService.createPuzzleQuestion(dto);
        enrichWithCompleteUrl(question, request);
        return new ResponseEntity<>(question, HttpStatus.CREATED);
    }

    @PostMapping(value = "/text", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<QuestionTypeText> createTextQuestion(
            @ModelAttribute QuestionTypeTextDTO dto,
            HttpServletRequest request) throws IOException {
        QuestionTypeText question = questionService.createTextQuestion(dto);
        enrichWithCompleteUrl(question, request);
        return new ResponseEntity<>(question, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions(HttpServletRequest request) {
        List<Question> questions = questionService.getAllQuestions();
        questions.forEach(q -> enrichWithCompleteUrl(q, request));
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<Question>> getQuestionsByQuizId(@PathVariable Long quizId, HttpServletRequest request) {
        List<Question> questions = questionService.getQuestionsByQuizIdOrderedByPosition(quizId);
        questions.forEach(q -> enrichWithCompleteUrl(q, request));
        return ResponseEntity.ok(questions);
    }

    @DeleteMapping("/quiz/{quizId}")
    public ResponseEntity<?> deleteAllQuestionsByQuizId(@PathVariable Long quizId) throws IOException {
        if (!questionService.existsByQuizId(quizId)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No questions found for quiz ID " + quizId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        questionService.deleteAllQuestionsByQuizId(quizId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/quiz/{quizId}/typed")
    public ResponseEntity<List<Object>> getAllTypedQuestionsByQuizId(@PathVariable Long quizId, HttpServletRequest request) {
        List<Object> questions = questionService.getAllTypedQuestionsByQuizId(quizId);
        questions.forEach(q -> {
            if (q instanceof Question) {
                enrichWithCompleteUrl((Question) q, request);
            }
        });
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id, HttpServletRequest request) {
        return questionService.getQuestionById(id)
                .map(question -> {
                    enrichWithCompleteUrl(question, request);
                    return ResponseEntity.ok(question);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) throws IOException {
        if (!questionService.existsById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Question with ID " + id + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateQuestion(
            @PathVariable Long id,
            @ModelAttribute QuestionDTO questionDTO,
            HttpServletRequest request) {
        try {
            Question updatedQuestion = questionService.updateQuestion(id, questionDTO);
            enrichWithCompleteUrl(updatedQuestion, request);
            return ResponseEntity.ok(updatedQuestion);
        } catch (Exception e) {
            return handleNotFound(e);
        }
    }

    @PutMapping(value = "/true-false/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateTrueFalseQuestion(
            @PathVariable Long id,
            @ModelAttribute QuestionTrueFalseDTO dto,
            HttpServletRequest request) {
        try {
            QuestionTrueFalse updatedQuestion = questionService.updateTrueFalseQuestion(id, dto);
            enrichWithCompleteUrl(updatedQuestion, request);
            return ResponseEntity.ok(updatedQuestion);
        } catch (Exception e) {
            return handleNotFound(e);
        }
    }

    @PutMapping(value = "/choice/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateChoiceQuestion(
            @PathVariable Long id,
            @ModelAttribute QuestionChoiceDTO dto,
            HttpServletRequest request) {
        try {
            QuestionChoice updatedQuestion = questionService.updateChoiceQuestion(id, dto);
            enrichWithCompleteUrl(updatedQuestion, request);
            return ResponseEntity.ok(updatedQuestion);
        } catch (Exception e) {
            return handleNotFound(e);
        }
    }

    @PutMapping(value = "/slider/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateSliderQuestion(
            @PathVariable Long id,
            @ModelAttribute QuestionSliderDTO dto,
            HttpServletRequest request) {
        try {
            QuestionSlider updatedQuestion = questionService.updateSliderQuestion(id, dto);
            enrichWithCompleteUrl(updatedQuestion, request);
            return ResponseEntity.ok(updatedQuestion);
        } catch (Exception e) {
            return handleNotFound(e);
        }
    }

    @PutMapping(value = "/puzzle/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePuzzleQuestion(
            @PathVariable Long id,
            @ModelAttribute QuestionPuzzleDTO dto,
            HttpServletRequest request) {
        try {
            QuestionPuzzle updatedQuestion = questionService.updatePuzzleQuestion(id, dto);
            enrichWithCompleteUrl(updatedQuestion, request);
            return ResponseEntity.ok(updatedQuestion);
        } catch (Exception e) {
            return handleNotFound(e);
        }
    }

    @PutMapping(value = "/text/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateTextQuestion(
            @PathVariable Long id,
            @ModelAttribute QuestionTypeTextDTO dto,
            HttpServletRequest request) {
        try {
            QuestionTypeText updatedQuestion = questionService.updateTextQuestion(id, dto);
            enrichWithCompleteUrl(updatedQuestion, request);
            return ResponseEntity.ok(updatedQuestion);
        } catch (Exception e) {
            return handleNotFound(e);
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", e.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, Object>> handleIOException(IOException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Error processing file: " + e.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private ResponseEntity<Map<String, Object>> handleNotFound(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", e.getMessage());
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    private void enrichWithCompleteUrl(Question question, HttpServletRequest request) {
        if (question.getImage() != null) {
            question.setImage(urlService.getCompleteFileUrl(question.getImage(), request));
        }
        if (question.getAudio() != null) {
            question.setAudio(urlService.getCompleteFileUrl(question.getAudio(), request));
        }
    }
}
