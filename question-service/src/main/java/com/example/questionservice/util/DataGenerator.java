package com.example.questionservice.util;

import com.github.javafaker.Faker;

import com.example.questionservice.client.QuizClient;
import com.example.questionservice.model.*;
import com.example.questionservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DataGenerator implements CommandLineRunner {
    private final QuestionRepository questionRepository;
    private final QuestionTypeRepository questionTypeRepository;
    private final QuestionTrueFalseRepository questionTrueFalseRepository;
    private final QuestionChoiceRepository questionChoiceRepository;
    private final QuestionSliderRepository questionSliderRepository;
    private final QuestionPuzzleRepository questionPuzzleRepository;
    private final QuestionTypeTextRepository questionTypeTextRepository;
    private final QuizClient quizClient;
    private final Faker faker = new Faker();
    private final Random random = new Random();

    @Override
    public void run(String... args) {
        // Create question types if they don't exist
        initializeQuestionTypes();

        // Generate questions if none exist
        if (questionRepository.count() == 0) {
            try {
                generateQuestions();
            } catch (Exception e) {
                System.err.println("Error generating questions: " + e.getMessage());
            }
        }
    }

    private void initializeQuestionTypes() {
        List<String> questionTypeNames = Arrays.asList(
                "TRUE_FALSE", "CHOICE", "SLIDER", "PUZZLE", "TEXT"
        );

        for (String name : questionTypeNames) {
            if (questionTypeRepository.findByName(name).isEmpty()) {
                QuestionType questionType = QuestionType.builder()
                        .name(name)
                        .build();
                questionTypeRepository.save(questionType);
            }
        }

        System.out.println("Question types initialized");
    }

    private void generateQuestions() {
        Map<String, QuestionType> questionTypes = new HashMap<>();
        questionTypeRepository.findAll().forEach(type -> questionTypes.put(type.getName(), type));

        // Assume we have 30 quizzes from IDs 1-30
        for (long quizId = 1; quizId <= 30; quizId++) {
            try {
                // In a real scenario, check if quiz exists using the client
                // For simplicity, we'll assume all quizzes exist

                // Generate 3-7 questions per quiz
                int questionsPerQuiz = random.nextInt(5) + 3;
                for (int i = 0; i < questionsPerQuiz; i++) {
                    // Randomly select question type
                    String[] types = {"TRUE_FALSE", "CHOICE", "SLIDER", "PUZZLE", "TEXT"};
                    String questionType = types[random.nextInt(types.length)];

                    generateQuestionByType(quizId, questionTypes.get(questionType));
                }
            } catch (Exception e) {
                System.err.println("Error generating questions for quiz " + quizId + ": " + e.getMessage());
            }
        }

        System.out.println("Generated questions for quizzes");
    }

    private void generateQuestionByType(Long quizId, QuestionType questionType) {
        switch (questionType.getName()) {
            case "TRUE_FALSE":
                generateTrueFalseQuestion(quizId, questionType);
                break;
            case "CHOICE":
                generateChoiceQuestion(quizId, questionType);
                break;
            case "SLIDER":
                generateSliderQuestion(quizId, questionType);
                break;
            case "PUZZLE":
                generatePuzzleQuestion(quizId, questionType);
                break;
            case "TEXT":
                generateTextQuestion(quizId, questionType);
                break;
            default:
                throw new IllegalStateException("Unexpected question type: " + questionType.getName());
        }
    }

    private void generateTrueFalseQuestion(Long quizId, QuestionType questionType) {
        QuestionTrueFalse question = QuestionTrueFalse.builder()
                .quizId(quizId)
                .questionType(questionType)
                .content(faker.lorem().sentence(10))
                .description(faker.lorem().paragraph())
                .timeLimit((long) (random.nextInt(60) + 30))
                .createdAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                .updatedAt(LocalDateTime.now())
                .correctAnswer(random.nextBoolean())
                .build();

        questionTrueFalseRepository.save(question);
    }

    private void generateChoiceQuestion(Long quizId, QuestionType questionType) {
        QuestionChoice question = QuestionChoice.builder()
                .quizId(quizId)
                .questionType(questionType)
                .content(faker.lorem().sentence(10))
                .description(faker.lorem().paragraph())
                .timeLimit((long) (random.nextInt(60) + 30))
                .createdAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                .updatedAt(LocalDateTime.now())
                .build();

        // Generate 2-5 options
        int optionCount = random.nextInt(4) + 2;
        List<ChoiceOption> options = new ArrayList<>();
        boolean hasCorrectAnswer = false;

        for (int i = 0; i < optionCount; i++) {
            boolean isCorrect = false;

            // Ensure at least one option is correct
            if (i == optionCount - 1 && !hasCorrectAnswer) {
                isCorrect = true;
            } else if (!hasCorrectAnswer) {
                isCorrect = random.nextBoolean();
                if (isCorrect) {
                    hasCorrectAnswer = true;
                }
            }

            ChoiceOption option = ChoiceOption.builder()
                    .text(faker.lorem().sentence())
                    .isCorrect(isCorrect)
                    .build();

            options.add(option);
        }

        question.setChoiceOptions(options);
        questionChoiceRepository.save(question);
    }

    private void generateSliderQuestion(Long quizId, QuestionType questionType) {
        int minValue = random.nextInt(50);
        int maxValue = minValue + random.nextInt(100) + 50;
        int correctAnswer = minValue + random.nextInt(maxValue - minValue);

        QuestionSlider question = QuestionSlider.builder()
                .quizId(quizId)
                .questionType(questionType)
                .content(faker.lorem().sentence(10))
                .description(faker.lorem().paragraph())
                .timeLimit((long) (random.nextInt(60) + 30))
                .createdAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                .updatedAt(LocalDateTime.now())
                .minValue(minValue)
                .maxValue(maxValue)
                .defaultValue((minValue + maxValue) / 2)
                .correctAnswer(correctAnswer)
                .lambda(5)
                .build();

        questionSliderRepository.save(question);
    }

    private void generatePuzzleQuestion(Long quizId, QuestionType questionType) {
        QuestionPuzzle question = QuestionPuzzle.builder()
                .quizId(quizId)
                .questionType(questionType)
                .content(faker.lorem().sentence(10))
                .description(faker.lorem().paragraph())
                .timeLimit((long) (random.nextInt(60) + 30))
                .createdAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                .updatedAt(LocalDateTime.now())
                .build();

        // Generate 4-9 puzzle pieces
        int pieceCount = random.nextInt(6) + 4;
        List<PuzzleOption> puzzlePieces = new ArrayList<>();

        for (int i = 0; i < pieceCount; i++) {
            PuzzleOption piece = PuzzleOption.builder()
                    .correctPosition(i)
                    .build();

            puzzlePieces.add(piece);
        }

        question.setPuzzlePieces(puzzlePieces);
        questionPuzzleRepository.save(question);
    }

    private void generateTextQuestion(Long quizId, QuestionType questionType) {
        QuestionTypeText question = QuestionTypeText.builder()
                .quizId(quizId)
                .questionType(questionType)
                .content(faker.lorem().sentence(10))
                .description(faker.lorem().paragraph())
                .timeLimit((long) (random.nextInt(60) + 30))
                .createdAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                .updatedAt(LocalDateTime.now())
                .caseSensitive(random.nextBoolean())
                .build();

        // Generate 1-3 accepted answers
        int answerCount = random.nextInt(3) + 1;
        List<String> acceptedAnswers = new ArrayList<>();

        for (int i = 0; i < answerCount; i++) {
            acceptedAnswers.add(faker.lorem().word());
        }

        question.setAcceptedAnswers(acceptedAnswers);
        questionTypeTextRepository.save(question);
    }
}
