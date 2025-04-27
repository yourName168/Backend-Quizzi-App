package com.example.questionservice.controller;

import com.example.questionservice.dto.*;
import com.example.questionservice.model.*;
import com.example.questionservice.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@Tag(name = "Question Management", description = "Operations related to question management")
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    @Operation(summary = "Create a new question", description = "Creates a new question with the provided question data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Question created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid question data provided")
    })
    public ResponseEntity<Question> createQuestion(@RequestBody @Parameter(description = "Question data to create") QuestionDTO questionDTO) {
        return new ResponseEntity<>(questionService.createQuestion(questionDTO), HttpStatus.CREATED);
    }

    @PostMapping("/true-false")
    @Operation(summary = "Create a true/false question", description = "Creates a new true/false question")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "True/false question created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid question data provided")
    })
    public ResponseEntity<QuestionTrueFalse> createTrueFalseQuestion(@RequestBody @Parameter(description = "True/false question data") QuestionTrueFalseDTO dto) {
        return new ResponseEntity<>(questionService.createTrueFalseQuestion(dto), HttpStatus.CREATED);
    }

    @PostMapping("/choice")
    @Operation(summary = "Create a multiple choice question", description = "Creates a new multiple choice question")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Multiple choice question created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid question data provided")
    })
    public ResponseEntity<QuestionChoice> createChoiceQuestion(@RequestBody @Parameter(description = "Multiple choice question data") QuestionChoiceDTO dto) {
        return new ResponseEntity<>(questionService.createChoiceQuestion(dto), HttpStatus.CREATED);
    }

    @PostMapping("/slider")
    @Operation(summary = "Create a slider question", description = "Creates a new slider question")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Slider question created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid question data provided")
    })
    public ResponseEntity<QuestionSlider> createSliderQuestion(@RequestBody @Parameter(description = "Slider question data") QuestionSliderDTO dto) {
        return new ResponseEntity<>(questionService.createSliderQuestion(dto), HttpStatus.CREATED);
    }

    @PostMapping("/puzzle")
    @Operation(summary = "Create a puzzle question", description = "Creates a new puzzle question")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Puzzle question created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid question data provided")
    })
    public ResponseEntity<QuestionPuzzle> createPuzzleQuestion(@RequestBody @Parameter(description = "Puzzle question data") QuestionPuzzleDTO dto) {
        return new ResponseEntity<>(questionService.createPuzzleQuestion(dto), HttpStatus.CREATED);
    }

    @PostMapping("/text")
    @Operation(summary = "Create a text question", description = "Creates a new text question")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Text question created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid question data provided")
    })
    public ResponseEntity<QuestionTypeText> createTextQuestion(@RequestBody @Parameter(description = "Text question data") QuestionTypeTextDTO dto) {
        return new ResponseEntity<>(questionService.createTextQuestion(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all questions", description = "Retrieves a list of all questions")
    @ApiResponse(responseCode = "200", description = "List of questions retrieved successfully")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get question by ID", description = "Retrieves a question by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Question found"),
        @ApiResponse(responseCode = "404", description = "Question not found")
    })
    public ResponseEntity<Question> getQuestionById(@PathVariable @Parameter(description = "Question ID") Long id) {
        return questionService.getQuestionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/quiz/{quizId}")
    @Operation(summary = "Get questions by quiz ID", description = "Retrieves all questions belonging to a specific quiz")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Questions retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    public ResponseEntity<List<Question>> getQuestionsByQuizId(@PathVariable @Parameter(description = "Quiz ID") Long quizId) {
        return ResponseEntity.ok(questionService.getQuestionsByQuizId(quizId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete question", description = "Deletes a question by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Question deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Question not found")
    })
    public ResponseEntity<Void> deleteQuestion(@PathVariable @Parameter(description = "Question ID to delete") Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}