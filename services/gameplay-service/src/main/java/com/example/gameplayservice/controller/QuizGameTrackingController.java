package com.example.gameplayservice.controller;

import com.example.gameplayservice.model.QuizGameTracking;
import com.example.gameplayservice.service.QuizGameTrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(name = "Quiz Game Tracking", description = "API endpoints for managing quiz game tracking data")
public class QuizGameTrackingController {
    private final QuizGameTrackingService quizGameTrackingService;

    @Operation(summary = "Create a new quiz game tracking record", description = "Creates a new tracking record for a quiz game session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tracking record created successfully", content = @Content(schema = @Schema(implementation = QuizGameTracking.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<QuizGameTracking> createQuizGameTracking(@RequestBody QuizGameTracking quizGameTracking) {
        return new ResponseEntity<>(quizGameTrackingService.createQuizGameTracking(quizGameTracking),
                HttpStatus.CREATED);
    }

    @Operation(summary = "Get all quiz game tracking records", description = "Retrieves all quiz game tracking records from the database")
    @ApiResponse(responseCode = "200", description = "Retrieved all tracking records successfully")
    @GetMapping
    public ResponseEntity<List<QuizGameTracking>> getAllQuizGameTrackings() {
        return ResponseEntity.ok(quizGameTrackingService.getAllQuizGameTrackings());
    }

    @Operation(summary = "Get a quiz game tracking record by ID", description = "Retrieves a specific quiz game tracking record based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tracking record found"),
            @ApiResponse(responseCode = "404", description = "Tracking record not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<QuizGameTracking> getQuizGameTrackingById(
            @Parameter(description = "ID of the tracking record to retrieve") @PathVariable Long id) {
        return quizGameTrackingService.getQuizGameTrackingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get tracking records by quiz ID", description = "Retrieves all tracking records associated with a specific quiz")
    @ApiResponse(responseCode = "200", description = "Retrieved tracking records successfully")
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<QuizGameTracking>> getQuizGameTrackingsByQuizId(
            @Parameter(description = "ID of the quiz to filter by") @PathVariable Long quizId) {
        return ResponseEntity.ok(quizGameTrackingService.getQuizGameTrackingsByQuizId(quizId));
    }

    @Operation(summary = "Get tracking records by user ID", description = "Retrieves all tracking records for a specific user")
    @ApiResponse(responseCode = "200", description = "Retrieved tracking records successfully")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<QuizGameTracking>> getQuizGameTrackingsByUserId(
            @Parameter(description = "ID of the user to filter by") @PathVariable Long userId) {
        return ResponseEntity.ok(quizGameTrackingService.getQuizGameTrackingsByUserId(userId));
    }

    @Operation(summary = "Delete a quiz game tracking record", description = "Deletes a quiz game tracking record by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tracking record deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Tracking record not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizGameTracking(
            @Parameter(description = "ID of the tracking record to delete") @PathVariable Long id) {
        quizGameTrackingService.deleteQuizGameTracking(id);
        return ResponseEntity.noContent().build();
    }
}