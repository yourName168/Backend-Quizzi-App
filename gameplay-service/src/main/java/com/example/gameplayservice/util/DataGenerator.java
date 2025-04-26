package com.example.gameplayservice.util;

import com.github.javafaker.Faker;
import com.example.gameplayservice.model.Participant;
import com.example.gameplayservice.model.QuestionTracking;
import com.example.gameplayservice.model.QuizGameTracking;
import com.example.gameplayservice.repository.ParticipantRepository;
import com.example.gameplayservice.repository.QuestionTrackingRepository;
import com.example.gameplayservice.repository.QuizGameTrackingRepository;
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
    private final QuizGameTrackingRepository quizGameTrackingRepository;
    private final ParticipantRepository participantRepository;
    private final QuestionTrackingRepository questionTrackingRepository;
    private final Faker faker = new Faker();
    private final Random random = new Random();

    @Override
    public void run(String... args) {
        if (quizGameTrackingRepository.count() == 0) {
            generateQuizGameTrackings(25);
            generateQuestionTrackings();
        }
    }

    private void generateQuizGameTrackings(int count) {
        List<QuizGameTracking> quizGameTrackings = new ArrayList<>();

        try {
            for (int i = 0; i < count; i++) {
                Long quizId = (long) (random.nextInt(30) + 1);
                Long userId = (long) (random.nextInt(10) + 1);

                QuizGameTracking quizGameTracking = QuizGameTracking.builder()
                        .quizId(quizId)
                        .userId(userId)
                        .totalPoints(random.nextInt(1000))
                        .rank(random.nextInt(100) + 1)
                        .currentStreak(random.nextInt(10))
                        .bestStreak(random.nextInt(15) + 5)
                        .build();

                quizGameTrackings.add(quizGameTracking);
            }

            quizGameTrackingRepository.saveAll(quizGameTrackings);

            for (QuizGameTracking quizGameTracking : quizGameTrackings) {
                int participantCount = random.nextInt(8) + 2;

                for (int i = 0; i < participantCount; i++) {
                    Long userId = (long) (random.nextInt(10) + 1);

                    if (participantRepository.findByQuizGameAndUserId(quizGameTracking, userId).isPresent()) {
                        continue;
                    }

                    Participant participant = Participant.builder()
                            .quizGame(quizGameTracking)
                            .userId(userId)
                            .build();

                    participantRepository.save(participant);
                }
            }

            System.out.println("Generated " + count + " quiz game trackings with participants");
        } catch (Exception e) {
            System.err.println("Error generating quiz game trackings: " + e.getMessage());
        }
    }

    private void generateQuestionTrackings() {
        try {
            List<QuizGameTracking> quizGameTrackings = quizGameTrackingRepository.findAll();

            for (QuizGameTracking quizGameTracking : quizGameTrackings) {
                List<Participant> participants = participantRepository.findByQuizGame(quizGameTracking);

                int questionCount = random.nextInt(6) + 5;

                for (Participant participant : participants) {
                    for (int i = 1; i <= questionCount; i++) {
                        Long questionId = (long) i;
                        boolean isCorrect = random.nextBoolean();
                        int timeTaken = random.nextInt(60) + 1;

                        int pointsEarned = 0;
                        if (isCorrect) {
                            pointsEarned = 100 + Math.max(0, 60 - timeTaken) * 5;
                        }

                        QuestionTracking questionTracking = QuestionTracking.builder()
                                .quizGame(quizGameTracking)
                                .userId(participant.getUserId())
                                .questionId(questionId)
                                .isCorrect(isCorrect)
                                .answeredAt(LocalDateTime.now().minusMinutes(random.nextInt(60)))
                                .timeTaken(timeTaken)
                                .pointsEarned(pointsEarned)
                                .responseChosen(faker.lorem().word())
                                .build();

                        questionTrackingRepository.save(questionTracking);
                    }
                }
            }

            System.out.println("Generated question tracking data");
        } catch (Exception e) {
            System.err.println("Error generating question trackings: " + e.getMessage());
        }
    }
}
