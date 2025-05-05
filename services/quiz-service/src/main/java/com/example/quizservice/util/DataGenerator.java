package com.example.quizservice.util;
import com.github.javafaker.Faker;

import com.example.quizservice.client.UserClient;
import com.example.quizservice.model.Quiz;
import com.example.quizservice.model.QuizCollection;
import com.example.quizservice.model.QuizGame;
import com.example.quizservice.repository.QuizCollectionRepository;
import com.example.quizservice.repository.QuizGameRepository;
import com.example.quizservice.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DataGenerator implements CommandLineRunner {
    private final QuizRepository quizRepository;
    private final QuizGameRepository quizGameRepository;
    private final QuizCollectionRepository quizCollectionRepository;
    private final UserClient userClient;
    private final Faker faker = new Faker();
    private final Random random = new Random();

    private static final String[] QUIZ_CATEGORIES = {
            "Education", "Entertainment", "Sports", "Science", "Technology",
            "History", "Geography", "Music", "Movies", "General Knowledge"
    };

    @Override
    public void run(String... args) {
        // Generate data if none exists
        if (quizRepository.count() == 0) {
            generateQuizzes(30);
            generateQuizGames(20);
            generateQuizCollections(15);
        }
    }

    private void generateQuizzes(int count) {
        List<Quiz> quizzes = new ArrayList<>();

        try {
            // Fetch some user IDs from user service (assuming we have at least 10 users)
            // In a real scenario, we would make a proper API call to get all user IDs
            // For simplicity, we'll assume users with IDs 1-10 exist
            for (int i = 0; i < count; i++) {
                Long userId = (long) (random.nextInt(10) + 1);

                Quiz quiz = Quiz.builder()
                        .userId(userId)
                        .title(faker.lorem().sentence(3))
                        .description(faker.lorem().paragraph())
                        .keyword(faker.lorem().words(3).toString())
                        .score(random.nextInt(100))
                        .coverPhoto("https://picsum.photos/id/" + random.nextInt(1000) + "/200/300")
                        .createdAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                        .updatedAt(LocalDateTime.now().minusDays(random.nextInt(10)))
                        .visible(random.nextBoolean())
                        .visibleQuizQuestion(random.nextBoolean())
                        .shuffle(random.nextBoolean())
                        .build();

                quizzes.add(quiz);
            }

            quizRepository.saveAll(quizzes);
            System.out.println("Generated " + count + " quizzes");
        } catch (Exception e) {
            System.err.println("Error generating quizzes: " + e.getMessage());
        }
    }

    private void generateQuizGames(int count) {
        List<QuizGame> quizGames = new ArrayList<>();
        List<Quiz> quizzes = quizRepository.findAll();

        if (quizzes.isEmpty()) {
            System.out.println("No quizzes found. Skipping quiz game generation.");
            return;
        }

        try {
            for (int i = 0; i < count; i++) {
                Quiz quiz = quizzes.get(random.nextInt(quizzes.size()));

                // Generate a unique 6-character code
                String code = generateUniqueCode();

                QuizGame quizGame = QuizGame.builder()
                        .adminId(quiz.getUserId()) // Admin is the creator of the quiz
                        .quiz(quiz)
                        .startedAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                        .code(code)
                        .qrCode("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + code)
                        .totalParticipants(random.nextInt(50))
                        .build();

                quizGames.add(quizGame);
            }

            quizGameRepository.saveAll(quizGames);
            System.out.println("Generated " + count + " quiz games");
        } catch (Exception e) {
            System.err.println("Error generating quiz games: " + e.getMessage());
        }
    }

    private void generateQuizCollections(int count) {
        List<QuizCollection> quizCollections = new ArrayList<>();
        List<Quiz> quizzes = quizRepository.findAll();

        if (quizzes.isEmpty()) {
            System.out.println("No quizzes found. Skipping quiz collection generation.");
            return;
        }

        try {
            for (int i = 0; i < count; i++) {
                Quiz quiz = quizzes.get(random.nextInt(quizzes.size()));

                QuizCollection quizCollection = QuizCollection.builder()
                        .authorId((long) (random.nextInt(10) + 1)) // Random user ID between 1 and 10
                        .quiz(quiz)
                        .description(faker.lorem().paragraph())
                        .category(QUIZ_CATEGORIES[random.nextInt(QUIZ_CATEGORIES.length)])
                        .visibleTo(random.nextBoolean())
                        .timestamp(LocalDateTime.now().minusDays(random.nextInt(30)))
                        .build();

                quizCollections.add(quizCollection);
            }

            quizCollectionRepository.saveAll(quizCollections);
            System.out.println("Generated " + count + " quiz collections");
        } catch (Exception e) {
            System.err.println("Error generating quiz collections: " + e.getMessage());
        }
    }

    private String generateUniqueCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }

        return code.toString();
    }
}