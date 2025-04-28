package com.example.quizservice.controller;

import com.example.quizservice.dto.QuizDTO;
import com.example.quizservice.model.Quiz;
import com.example.quizservice.service.QuizService;
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
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
@Tag(name = "Quiz Management", description = "Operations related to quiz management")
public class QuizController {

    private final QuizService quizService;

    @PostMapping
    @Operation(summary = "Create a new quiz", description = "Creates a new quiz with the provided quiz data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Quiz created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid quiz data provided")
    })
    public ResponseEntity<Quiz> createQuiz(@RequestBody @Parameter(description = "Quiz data to create") QuizDTO quizDTO) {
        return new ResponseEntity<>(quizService.createQuiz(quizDTO), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all quizzes", description = "Retrieves a list of all quizzes")
    @ApiResponse(responseCode = "200", description = "List of quizzes retrieved successfully")
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get quizzes by user ID", description = "Retrieves all quizzes created by a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quizzes retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<Quiz>> getQuizzesByUserId(@PathVariable @Parameter(description = "User ID") Long userId) {
        return ResponseEntity.ok(quizService.getQuizzesByUserId(userId));
    }

    @GetMapping("/public")
    @Operation(summary = "Get public quizzes", description = "Retrieves all public quizzes")
    @ApiResponse(responseCode = "200", description = "Public quizzes retrieved successfully")
    public ResponseEntity<List<Quiz>> getPublicQuizzes() {
        return ResponseEntity.ok(quizService.getPublicQuizzes());
    }

    @GetMapping("/search")
    @Operation(summary = "Search quizzes", description = "Search quizzes by title")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    public ResponseEntity<List<Quiz>> searchQuizzes(@RequestParam @Parameter(description = "Search query") String title) {
        return ResponseEntity.ok(quizService.searchQuizzes(title));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update quiz", description = "Updates an existing quiz with the provided data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz updated successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    public ResponseEntity<Quiz> updateQuiz(
            @PathVariable @Parameter(description = "Quiz ID to update") Long id,
            @RequestBody @Parameter(description = "Updated quiz data") QuizDTO quizDTO) {
        return ResponseEntity.ok(quizService.updateQuiz(id, quizDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete quiz", description = "Deletes a quiz by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Quiz deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    public ResponseEntity<Void> deleteQuiz(@PathVariable @Parameter(description = "Quiz ID to delete") Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get quiz by ID", description = "Retrieves a quiz by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz found"),
        @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    public ResponseEntity<Quiz> getQuizById(@PathVariable @Parameter(description = "Quiz ID") Long id) {
        return quizService.getQuizById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
