package com.example.gameplayservice.service;

import com.example.gameplayservice.client.UserClient;
import com.example.gameplayservice.dto.ParticipantDTO;
import com.example.gameplayservice.model.Participant;
import com.example.gameplayservice.model.QuizGameTracking;
import com.example.gameplayservice.repository.ParticipantRepository;
import com.example.gameplayservice.repository.QuizGameTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final QuizGameTrackingRepository quizGameTrackingRepository;
    private final UserClient userClient;

    public Participant addParticipant(ParticipantDTO participantDTO) {
        QuizGameTracking quizGameTracking = quizGameTrackingRepository.findById(participantDTO.getQuizGameId())
                .orElseThrow(() -> new RuntimeException("Quiz game tracking not found"));

        try {
            Object user = userClient.getUserById(participantDTO.getUserId());
            if (user == null) {
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking user: " + e.getMessage());
        }

        // Check if participant already exists
        Optional<Participant> existingParticipant = participantRepository.findByQuizGameAndUserId(
                quizGameTracking, participantDTO.getUserId());

        if (existingParticipant.isPresent()) {
            return existingParticipant.get();
        }

        // Create new participant
        Participant participant = Participant.builder()
                .quizGame(quizGameTracking)
                .userId(participantDTO.getUserId())
                .build();

        return participantRepository.save(participant);
    }

    public List<Participant> getParticipantsByQuizGame(Long quizGameId) {
        QuizGameTracking quizGameTracking = quizGameTrackingRepository.findById(quizGameId)
                .orElseThrow(() -> new RuntimeException("Quiz game tracking not found"));

        return participantRepository.findByQuizGame(quizGameTracking);
    }

    public Optional<Participant> getParticipant(Long quizGameId, Long userId) {
        QuizGameTracking quizGameTracking = quizGameTrackingRepository.findById(quizGameId)
                .orElseThrow(() -> new RuntimeException("Quiz game tracking not found"));

        return participantRepository.findByQuizGameAndUserId(quizGameTracking, userId);
    }

    public void removeParticipant(Long participantId) {
        participantRepository.deleteById(participantId);
    }

    public int getParticipantCount(Long quizGameId) {
        QuizGameTracking quizGameTracking = quizGameTrackingRepository.findById(quizGameId)
                .orElseThrow(() -> new RuntimeException("Quiz game tracking not found"));

        return participantRepository.countByQuizGame(quizGameTracking);
    }
}

