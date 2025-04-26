package com.example.quizservice.service;

import com.example.quizservice.dto.QuizGameDTO;
import com.example.quizservice.model.Quiz;
import com.example.quizservice.model.QuizGame;
import com.example.quizservice.repository.QuizGameRepository;
import com.example.quizservice.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class QuizGameService {
    private final QuizGameRepository quizGameRepository;
    private final QuizRepository quizRepository;

    public QuizGame createQuizGame(QuizGameDTO quizGameDTO) {
        Quiz quiz = quizRepository.findById(quizGameDTO.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        // Generate a unique 6-character code
        String code = generateUniqueCode();

        QuizGame quizGame = QuizGame.builder()
                .adminId(quizGameDTO.getAdminId())
                .quiz(quiz)
                .startedAt(LocalDateTime.now())
                .code(code)
                .qrCode("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + code)
                .totalParticipants(0)
                .build();

        return quizGameRepository.save(quizGame);
    }

    public List<QuizGame> getAllQuizGames() {
        return quizGameRepository.findAll();
    }

    public Optional<QuizGame> getQuizGameById(Long id) {
        return quizGameRepository.findById(id);
    }

    public List<QuizGame> getQuizGamesByQuizId(Long quizId) {
        return quizGameRepository.findByQuizId(quizId);
    }

    public List<QuizGame> getQuizGamesByAdminId(Long adminId) {
        return quizGameRepository.findByAdminId(adminId);
    }

    public Optional<QuizGame> getQuizGameByCode(String code) {
        return quizGameRepository.findByCode(code);
    }

    public void deleteQuizGame(Long id) {
        quizGameRepository.deleteById(id);
    }

    private String generateUniqueCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        boolean isUnique = false;

        while (!isUnique) {
            code = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                code.append(chars.charAt(random.nextInt(chars.length())));
            }

            if (quizGameRepository.findByCode(code.toString()).isEmpty()) {
                isUnique = true;
            }
        }

        return code.toString();
    }
}