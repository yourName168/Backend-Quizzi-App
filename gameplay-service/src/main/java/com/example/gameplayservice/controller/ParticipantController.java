package com.example.gameplayservice.controller;

import com.example.gameplayservice.dto.ParticipantDTO;
import com.example.gameplayservice.model.Participant;
import com.example.gameplayservice.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;

    @PostMapping
    public ResponseEntity<Participant> addParticipant(@RequestBody ParticipantDTO participantDTO) {
        return new ResponseEntity<>(participantService.addParticipant(participantDTO), HttpStatus.CREATED);
    }

    @GetMapping("/quiz-game/{quizGameId}")
    public ResponseEntity<List<Participant>> getParticipantsByQuizGame(@PathVariable Long quizGameId) {
        return ResponseEntity.ok(participantService.getParticipantsByQuizGame(quizGameId));
    }

    @GetMapping("/quiz-game/{quizGameId}/user/{userId}")
    public ResponseEntity<Participant> getParticipant(@PathVariable Long quizGameId, @PathVariable Long userId) {
        return participantService.getParticipant(quizGameId, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/quiz-game/{quizGameId}/count")
    public ResponseEntity<Integer> getParticipantCount(@PathVariable Long quizGameId) {
        return ResponseEntity.ok(participantService.getParticipantCount(quizGameId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeParticipant(@PathVariable Long id) {
        participantService.removeParticipant(id);
        return ResponseEntity.noContent().build();
    }
}