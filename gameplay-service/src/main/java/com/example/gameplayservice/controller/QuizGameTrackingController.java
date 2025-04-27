package com.example.gameplayservice.controller;

import com.example.gameplayservice.dto.QuizGameTrackingDTO;
import com.example.gameplayservice.model.QuizGameTracking;
import com.example.gameplayservice.service.QuizGameTrackingService;
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
@RequestMapping("/api/quiz-game-tracking")
@RequiredArgsConstructor
@Tag(name = "Quiz Game Tracking", description = "Operations related to tracking quiz game progress")
public class QuizGameTrackingController {
    private final QuizGameTrackingService quizGameTrackingService;

    @PostMapping
    @Operation(summary = "Create quiz game tracking", description = "Creates a new quiz game tracking record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Quiz game tracking created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid tracking data provided")
    })
    public ResponseEntity<QuizGameTracking> createQuizGameTracking(@RequestBody @Parameter(description = "Quiz game tracking data") QuizGameTrackingDTO dto) {
        return new ResponseEntity<>(quizGameTrackingService.createQuizGameTracking(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all quiz game trackings", description = "Retrieves a list of all quiz game tracking records")
    @ApiResponse(responseCode = "200", description = "List of quiz game trackings retrieved successfully")
    public ResponseEntity<List<QuizGameTracking>> getAllQuizGameTrackings() {
        return ResponseEntity.ok(quizGameTrackingService.getAllQuizGameTrackings());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get quiz game tracking by ID", description = "Retrieves a quiz game tracking record by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz game tracking found"),
        @ApiResponse(responseCode = "404", description = "Quiz game tracking not found")
    })
    public ResponseEntity<QuizGameTracking> getQuizGameTrackingById(@PathVariable @Parameter(description = "Quiz game tracking ID") Long id) {
        return quizGameTrackingService.getQuizGameTrackingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/quiz/{quizId}")
    @Operation(summary = "Get quiz game trackings by quiz ID", description = "Retrieves all quiz game tracking records for a specific quiz")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz game trackings retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    public ResponseEntity<List<QuizGameTracking>> getQuizGameTrackingsByQuizId(@PathVariable @Parameter(description = "Quiz ID") Long quizId) {
        return ResponseEntity.ok(quizGameTrackingService.getQuizGameTrackingsByQuizId(quizId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get quiz game trackings by user ID", description = "Retrieves all quiz game tracking records for a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz game trackings retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<QuizGameTracking>> getQuizGameTrackingsByUserId(@PathVariable @Parameter(description = "User ID") Long userId) {
        return ResponseEntity.ok(quizGameTrackingService.getQuizGameTrackingsByUserId(userId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update quiz game tracking", description = "Updates an existing quiz game tracking record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz game tracking updated successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz game tracking not found")
    })
    public ResponseEntity<QuizGameTracking> updateQuizGameTracking(
            @PathVariable @Parameter(description = "Quiz game tracking ID") Long id, 
            @RequestBody @Parameter(description = "Updated quiz game tracking data") QuizGameTracking quizGameTracking) {
        return ResponseEntity.ok(quizGameTrackingService.updateQuizGameTracking(id, quizGameTracking));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete quiz game tracking", description = "Deletes a quiz game tracking record by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Quiz game tracking deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz game tracking not found")
    })
    public ResponseEntity<Void> deleteQuizGameTracking(@PathVariable @Parameter(description = "Quiz game tracking ID to delete") Long id) {
        quizGameTrackingService.deleteQuizGameTracking(id);
        return ResponseEntity.noContent().build();
    }
}