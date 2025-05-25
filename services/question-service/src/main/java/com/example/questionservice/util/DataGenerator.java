package com.example.questionservice.util;

import com.github.javafaker.Faker;
import com.example.questionservice.client.QuizClient;
import com.example.questionservice.model.*;
import com.example.questionservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DataGenerator implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataGenerator.class);
    
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
    
    @Value("${app.data.reset:false}")
    private boolean resetData;

    @Override
    public void run(String... args) {
        // Remove @Transactional from here
        try {
            // if (resetData) {
            //     cleanupData();
            // }
            
            initializeQuestionTypes();

            // if (questionRepository.count() == 0) {
            //     generateAllQuestions();
            // }
        } catch (Exception e) {
            logger.error("Error in data generation: {}", e.getMessage(), e);
        }
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cleanupData() {
        logger.info("Cleaning up existing data");
        questionTrueFalseRepository.deleteAll();
        questionChoiceRepository.deleteAll();
        questionSliderRepository.deleteAll();
        questionPuzzleRepository.deleteAll();
        questionTypeTextRepository.deleteAll();
        questionRepository.deleteAll();
        questionTypeRepository.deleteAll();
        logger.info("Data cleanup complete");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void initializeQuestionTypes() {
        logger.info("Initializing question types");
        List<String> questionTypeNames = Arrays.asList(
                "TRUE_FALSE", "SINGLE_CHOICE", "MULTI_CHOICE", "SLIDER", "PUZZLE", "TEXT"
        );

        for (String name : questionTypeNames) {
            if (questionTypeRepository.findByName(name).isEmpty()) {
                QuestionType questionType = QuestionType.builder()
                        .name(name)
                        .build();
                questionTypeRepository.save(questionType);
            }
        }
        logger.info("Question types initialized");
    }

    public void generateAllQuestions() {
        logger.info("Starting question generation");
        Map<String, QuestionType> questionTypes = new HashMap<>();
        questionTypeRepository.findAll().forEach(type -> questionTypes.put(type.getName(), type));

        // Generate for each quiz in a separate transaction
        for (long quizId = 1; quizId <= 30; quizId++) {
            try {
                generateQuestionsForQuiz(quizId, questionTypes);
            } catch (Exception e) {
                logger.error("Error generating questions for quiz {}: {}", quizId, e.getMessage());
                // Continue with next quiz
            }
        }
        logger.info("Question generation completed");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void generateQuestionsForQuiz(Long quizId, Map<String, QuestionType> questionTypes) {
        int questionsPerQuiz = random.nextInt(5) + 3;
        logger.info("Generating {} questions for quiz {}", questionsPerQuiz, quizId);
        
        for (int i = 0; i < questionsPerQuiz; i++) {
            String[] types = {"TRUE_FALSE", "SINGLE_CHOICE", "MULTI_CHOICE", "SLIDER", "PUZZLE", "TEXT"};
            String questionType = types[random.nextInt(types.length)];
            
            // Pass position (i) to generateQuestionByType
            generateQuestionByType(quizId, questionTypes.get(questionType), i);
        }
    }

    private void generateQuestionByType(Long quizId, QuestionType questionType, int position) {
        switch (questionType.getName()) {
            case "TRUE_FALSE":
                generateTrueFalseQuestion(quizId, questionType, position);
                break;
            case "SINGLE_CHOICE":
                generateSingleChoiceQuestion(quizId, questionType, position);
                break;
            case "MULTI_CHOICE":
                generateMultiChoiceQuestion(quizId, questionType, position);
                break;
            case "SLIDER":
                generateSliderQuestion(quizId, questionType, position);
                break;
            case "PUZZLE":
                generatePuzzleQuestion(quizId, questionType, position);
                break;
            case "TEXT":
                generateTextQuestion(quizId, questionType, position);
                break;
            default:
                throw new IllegalStateException("Unexpected question type: " + questionType.getName());
        }
    }

    private void generateTrueFalseQuestion(Long quizId, QuestionType questionType, int position) {
        List<Integer> timeLimitList = Arrays.asList(5, 10, 20, 30, 45, 60, 90, 120);
        List<Integer> pointList = Arrays.asList(50, 100, 200, 250, 500, 750, 1000, 2000);
        QuestionTrueFalse question = QuestionTrueFalse.builder()
                .quizId(quizId)
                .questionType(questionType)
                .content(faker.lorem().sentence(10))
                .description(faker.lorem().paragraph())
                .timeLimit(timeLimitList.get(random.nextInt(timeLimitList.size())))
                .point(pointList.get(random.nextInt(pointList.size())))
                .createdAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                .updatedAt(LocalDateTime.now())
                .correctAnswer(random.nextBoolean())
                .position(position) 
                .build();

        questionTrueFalseRepository.save(question);
    }

    private void generateSingleChoiceQuestion(Long quizId, QuestionType questionType, int position) {
        List<Integer> timeLimitList = Arrays.asList(5, 10, 20, 30, 45, 60, 90, 120);
        List<Integer> pointList = Arrays.asList(50, 100, 200, 250, 500, 750, 1000, 2000);
        QuestionChoice question = QuestionChoice.builder()
                .quizId(quizId)
                .questionType(questionType)
                .content(faker.lorem().sentence(10))
                .description(faker.lorem().paragraph())
                .timeLimit(timeLimitList.get(random.nextInt(timeLimitList.size())))
                .point(pointList.get(random.nextInt(pointList.size())))
                .createdAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                .updatedAt(LocalDateTime.now())
                .position(position) // Set the position
                .build();

        // Generate 3-5 options with exactly one correct answer
        int optionCount = random.nextInt(3) + 3;
        List<ChoiceOption> options = new ArrayList<>();
        
        // Randomly select which option will be correct
        int correctOptionIndex = random.nextInt(optionCount);
        
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 4; i++) {
            boolean isCorrect = (i == correctOptionIndex);
            
            ChoiceOption option = ChoiceOption.builder()
                    .text(faker.lorem().sentence())
                    .isCorrect(isCorrect)
                    .createdAt(now)
                    .updatedAt(now)
                    .question(question)
                    .build();

            options.add(option);
        }

        question.setChoiceOptions(options);
        questionChoiceRepository.save(question);
    }

    private void generateMultiChoiceQuestion(Long quizId, QuestionType questionType, int position) {
        List<Integer> timeLimitList = Arrays.asList(5, 10, 20, 30, 45, 60, 90, 120);
        List<Integer> pointList = Arrays.asList(50, 100, 200, 250, 500, 750, 1000, 2000);
        QuestionChoice question = QuestionChoice.builder()
                .quizId(quizId)
                .questionType(questionType)
                .content(faker.lorem().sentence(10))
                .description(faker.lorem().paragraph())
                .timeLimit(timeLimitList.get(random.nextInt(timeLimitList.size())))
                .point(pointList.get(random.nextInt(pointList.size())))
                .createdAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                .updatedAt(LocalDateTime.now())
                .position(position) // Set the position
                .build();

        // Generate 4-6 options with multiple correct answers
        int optionCount = random.nextInt(3) + 4;
        List<ChoiceOption> options = new ArrayList<>();
        
        // Ensure at least 2 options are correct
        int correctCount = 0;
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < optionCount; i++) {
            // If we're at the last option and don't have enough correct answers yet,
            // force this one to be correct
            boolean isCorrect;
            if (i >= optionCount - 2 && correctCount < 2) {
                isCorrect = true;
                correctCount++;
            } else {
                // Otherwise 40% chance of being correct
                isCorrect = random.nextDouble() < 0.4;
                if (isCorrect) {
                    correctCount++;
                }
            }
            
            ChoiceOption option = ChoiceOption.builder()
                    .text(faker.lorem().sentence())
                    .isCorrect(isCorrect)
                    .createdAt(now)
                    .updatedAt(now)
                    .question(question)
                    .build();

            options.add(option);
        }

        question.setChoiceOptions(options);
        questionChoiceRepository.save(question);
    }

    private void generateSliderQuestion(Long quizId, QuestionType questionType, int position) {
        List<Integer> timeLimitList = Arrays.asList(5, 10, 20, 30, 45, 60, 90, 120);
        List<Integer> pointList = Arrays.asList(50, 100, 200, 250, 500, 750, 1000, 2000);
        int minValue = random.nextInt(50);
        int maxValue = minValue + random.nextInt(100) + 50;
        int correctAnswer = minValue + random.nextInt(maxValue - minValue);
        List<String> sliderItems = Arrays.asList("Orange", "Green", "Blue", "Primary");
        QuestionSlider question = QuestionSlider.builder()
                .quizId(quizId)
                .questionType(questionType)
                .content(faker.lorem().sentence(10))
                .description(faker.lorem().paragraph())
                .timeLimit(timeLimitList.get(random.nextInt(timeLimitList.size())))
                .point(pointList.get(random.nextInt(pointList.size())))
                .createdAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                .updatedAt(LocalDateTime.now())
                .minValue(minValue)
                .maxValue(maxValue)
                .defaultValue((minValue + maxValue) / 2)
                .correctAnswer(correctAnswer)
                .color(sliderItems.get(random.nextInt(sliderItems.size())))
                .position(position) // Set the position
                .build();

        questionSliderRepository.save(question);
    }

    private void generatePuzzleQuestion(Long quizId, QuestionType questionType, int position) {
        List<Integer> timeLimitList = Arrays.asList(5, 10, 20, 30, 45, 60, 90, 120);
        List<Integer> pointList = Arrays.asList(50, 100, 200, 250, 500, 750, 1000, 2000);
        QuestionPuzzle question = QuestionPuzzle.builder()
                .quizId(quizId)
                .questionType(questionType)
                .content(faker.lorem().sentence(10))
                .description(faker.lorem().paragraph())
                .timeLimit(timeLimitList.get(random.nextInt(timeLimitList.size())))
                .point(pointList.get(random.nextInt(pointList.size())))
                .createdAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                .updatedAt(LocalDateTime.now())
                .position(position) // Set the position
                .build();

        // Generate 4-9 puzzle pieces
        int pieceCount = random.nextInt(6) + 4;
        List<PuzzleOption> puzzlePieces = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < pieceCount; i++) {
            PuzzleOption piece = PuzzleOption.builder()
                    .correctPosition(i)
                    .text("Piece " + (i + 1))
                    .createdAt(now)
                    .updatedAt(now)
                    .question(question)
                    .build();

            puzzlePieces.add(piece);
        }

        question.setPuzzlePieces(puzzlePieces);
        questionPuzzleRepository.save(question);
    }

    private void generateTextQuestion(Long quizId, QuestionType questionType, int position) {
        List<Integer> timeLimitList = Arrays.asList(5, 10, 20, 30, 45, 60, 90, 120);
        List<Integer> pointList = Arrays.asList(50, 100, 200, 250, 500, 750, 1000, 2000);
        QuestionTypeText question = QuestionTypeText.builder()
                .quizId(quizId)
                .questionType(questionType)
                .content(faker.lorem().sentence(10))
                .description(faker.lorem().paragraph())
                .timeLimit(timeLimitList.get(random.nextInt(timeLimitList.size())))
                .point(pointList.get(random.nextInt(pointList.size())))
                .createdAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                .updatedAt(LocalDateTime.now())
                .caseSensitive(random.nextBoolean())
                .position(position) // Set the position
                .build();

        // Generate 1-3 accepted answers
        int answerCount = random.nextInt(3) + 1;
        List<TypeTextOption> acceptedAnswers = new ArrayList<>();

        for (int i = 0; i < answerCount; i++) {
            LocalDateTime now = LocalDateTime.now();
            TypeTextOption option = TypeTextOption.builder()
                    .text(faker.lorem().word())
                    .createdAt(now)
                    .updatedAt(now)
                    .question(question)
                    .build();
            acceptedAnswers.add(option);
        }

        question.setAcceptedAnswers(acceptedAnswers);
        questionTypeTextRepository.save(question);
    }
}