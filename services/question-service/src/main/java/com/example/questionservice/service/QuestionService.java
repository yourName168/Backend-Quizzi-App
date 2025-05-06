package com.example.questionservice.service;

import com.example.questionservice.client.QuizClient;
import com.example.questionservice.dto.*;
import com.example.questionservice.model.*;
import com.example.questionservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionTypeRepository questionTypeRepository;
    private final QuestionTrueFalseRepository questionTrueFalseRepository;
    private final QuestionChoiceRepository questionChoiceRepository;
    private final QuestionSliderRepository questionSliderRepository;
    private final QuestionPuzzleRepository questionPuzzleRepository;
    private final QuestionTypeTextRepository questionTypeTextRepository;
    private final QuizClient quizClient;

    public Question createQuestion(QuestionDTO questionDTO) {
        try {
            Object quiz = quizClient.getQuizById(questionDTO.getQuizId());
            if (quiz == null) {
                throw new RuntimeException("Quiz not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking quiz: " + e.getMessage());
        }

        QuestionType questionType = questionTypeRepository.findById(questionDTO.getQuestionTypeId())
                .orElseThrow(() -> new RuntimeException("Question type not found"));

        Question question = Question.builder()
                .quizId(questionDTO.getQuizId())
                .questionType(questionType)
                .image(questionDTO.getImage())
                .audio(questionDTO.getAudio())
                .content(questionDTO.getContent())
                .timeLimit(questionDTO.getTimeLimit())
                .description(questionDTO.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return questionRepository.save(question);
    }

    public QuestionTrueFalse createTrueFalseQuestion(QuestionTrueFalseDTO dto) {
        try {
            Object quiz = quizClient.getQuizById(dto.getQuizId());
            if (quiz == null) {
                throw new RuntimeException("Quiz not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking quiz: " + e.getMessage());
        }

        QuestionType questionType = questionTypeRepository.findByName("TRUE_FALSE")
                .orElseThrow(() -> new RuntimeException("Question type TRUE_FALSE not found"));

        QuestionTrueFalse question = QuestionTrueFalse.builder()
                .quizId(dto.getQuizId())
                .questionType(questionType)
                .image(dto.getImage())
                .audio(dto.getAudio())
                .content(dto.getContent())
                .timeLimit(dto.getTimeLimit())
                .point(dto.getPoint())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .correctAnswer(dto.getCorrectAnswer())
                .build();

        return questionTrueFalseRepository.save(question);
    }


    public QuestionChoice createChoiceQuestion(QuestionChoiceDTO dto) {
        try {
            Object quiz = quizClient.getQuizById(dto.getQuizId());
            if (quiz == null) {
                throw new RuntimeException("Quiz not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking quiz: " + e.getMessage());
        }

        String questionTypeName = "MULTI_CHOICE"; 
        
        int correctCount = 0;
        for (ChoiceOptionDTO option : dto.getChoiceOptions()) {
            if (option.getIsCorrect()) {
                correctCount++;
            }
        }
        
        if (correctCount == 1) {
            questionTypeName = "SINGLE_CHOICE";
        } else if (correctCount == 0 && !dto.getChoiceOptions().isEmpty()) {
            throw new RuntimeException("Choice questions must have at least one correct answer");
        }

        final String finalQuestionTypeName = questionTypeName;

        QuestionType questionType = questionTypeRepository.findByName(finalQuestionTypeName)
            .orElseThrow(() -> new RuntimeException("Question type " + finalQuestionTypeName + " not found"));
        
        QuestionChoice question = QuestionChoice.builder()
                .quizId(dto.getQuizId())
                .questionType(questionType)
                .image(dto.getImage())
                .audio(dto.getAudio())
                .content(dto.getContent())
                .timeLimit(dto.getTimeLimit())
                .point(dto.getPoint())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<ChoiceOption> choiceOptions = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (ChoiceOptionDTO optionDTO : dto.getChoiceOptions()) {
            ChoiceOption option = ChoiceOption.builder()
                    .text(optionDTO.getText())
                    .image(optionDTO.getImage())
                    .audio(optionDTO.getAudio())
                    .isCorrect(optionDTO.getIsCorrect())
                    .createdAt(now)
                    .updatedAt(now)
                    .question(question)
                    .build();
            choiceOptions.add(option);
        }

        question.setChoiceOptions(choiceOptions);

        return questionChoiceRepository.save(question);
    }

    public QuestionChoice createSingleChoiceQuestion(QuestionChoiceDTO dto) {
        boolean hasCorrectAnswer = false;
        for (ChoiceOptionDTO optionDTO : dto.getChoiceOptions()) {
            if (optionDTO.getIsCorrect()) {
                if (hasCorrectAnswer) {
                    throw new RuntimeException("Single choice questions can only have one correct answer");
                }
                hasCorrectAnswer = true;
            }
        }
        
        if (!hasCorrectAnswer && !dto.getChoiceOptions().isEmpty()) {
            throw new RuntimeException("Single choice questions must have exactly one correct answer");
        }
        
        QuestionType questionType = questionTypeRepository.findByName("SINGLE_CHOICE")
                .orElseThrow(() -> new RuntimeException("Question type SINGLE_CHOICE not found"));
        
        dto.setQuestionTypeId(questionType.getId());
        
        return createChoiceQuestion(dto);
    }

    public QuestionChoice createMultiChoiceQuestion(QuestionChoiceDTO dto) {
        boolean hasCorrectAnswer = false;
        for (ChoiceOptionDTO optionDTO : dto.getChoiceOptions()) {
            if (optionDTO.getIsCorrect()) {
                hasCorrectAnswer = true;
                break;
            }
        }
        
        if (!hasCorrectAnswer && !dto.getChoiceOptions().isEmpty()) {
            throw new RuntimeException("Multi-choice questions must have at least one correct answer");
        }
        
        QuestionType questionType = questionTypeRepository.findByName("MULTI_CHOICE")
                .orElseThrow(() -> new RuntimeException("Question type MULTI_CHOICE not found"));
        
        dto.setQuestionTypeId(questionType.getId());
        
        return createChoiceQuestion(dto);
    }

    public QuestionSlider createSliderQuestion(QuestionSliderDTO dto) {
        try {
            Object quiz = quizClient.getQuizById(dto.getQuizId());
            if (quiz == null) {
                throw new RuntimeException("Quiz not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking quiz: " + e.getMessage());
        }

        QuestionType questionType = questionTypeRepository.findByName("SLIDER")
                .orElseThrow(() -> new RuntimeException("Question type SLIDER not found"));

        QuestionSlider question = QuestionSlider.builder()
                .quizId(dto.getQuizId())
                .questionType(questionType)
                .image(dto.getImage())
                .audio(dto.getAudio())
                .content(dto.getContent())
                .timeLimit(dto.getTimeLimit())
                .point(dto.getPoint())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .minValue(dto.getMinValue())
                .maxValue(dto.getMaxValue())
                .defaultValue(dto.getDefaultValue())
                .correctAnswer(dto.getCorrectAnswer())
                .lambda(dto.getLambda())
                .build();

        return questionSliderRepository.save(question);
    }

    public QuestionPuzzle createPuzzleQuestion(QuestionPuzzleDTO dto) {
        try {
            Object quiz = quizClient.getQuizById(dto.getQuizId());
            if (quiz == null) {
                throw new RuntimeException("Quiz not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking quiz: " + e.getMessage());
        }

        QuestionType questionType = questionTypeRepository.findByName("PUZZLE")
                .orElseThrow(() -> new RuntimeException("Question type PUZZLE not found"));

        QuestionPuzzle question = QuestionPuzzle.builder()
                .quizId(dto.getQuizId())
                .questionType(questionType)
                .image(dto.getImage())
                .audio(dto.getAudio())
                .content(dto.getContent())
                .timeLimit(dto.getTimeLimit())
                .point(dto.getPoint())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<PuzzleOption> puzzleOptions = new ArrayList<>();
        for (PuzzleOptionDTO optionDTO : dto.getPuzzlePieces()) {
            PuzzleOption option = PuzzleOption.builder()
                    .correctPosition(optionDTO.getCorrectPosition())
                    .build();
            puzzleOptions.add(option);
        }

        question.setPuzzlePieces(puzzleOptions);

        return questionPuzzleRepository.save(question);
    }

    public QuestionTypeText createTextQuestion(QuestionTypeTextDTO dto) {
        try {
            Object quiz = quizClient.getQuizById(dto.getQuizId());
            if (quiz == null) {
                throw new RuntimeException("Quiz not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking quiz: " + e.getMessage());
        }

        QuestionType questionType = questionTypeRepository.findByName("TEXT")
                .orElseThrow(() -> new RuntimeException("Question type TEXT not found"));

        QuestionTypeText question = QuestionTypeText.builder()
                .quizId(dto.getQuizId())
                .questionType(questionType)
                .image(dto.getImage())
                .audio(dto.getAudio())
                .content(dto.getContent())
                .timeLimit(dto.getTimeLimit())
                .point(dto.getPoint())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .caseSensitive(dto.getCaseSensitive())
                .build();

        return questionTypeTextRepository.save(question);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public List<Question> getAllQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    public List<Object> getAllTypedQuestionsByQuizId(Long quizId) {
        List<Question> questions = questionRepository.findByQuizId(quizId);
        List<Object> typedQuestions = new ArrayList<>();
        
        for (Question question : questions) {
            String questionType = question.getQuestionType().getName();
            switch (questionType) {
                case "TRUE_FALSE":
                    typedQuestions.add(questionTrueFalseRepository.findById(question.getId())
                        .orElseThrow(() -> new RuntimeException("Question not found with id: " + question.getId())));
                    break;
                case "SINGLE_CHOICE":
                case "MULTI_CHOICE":
                    typedQuestions.add(questionChoiceRepository.findById(question.getId())
                        .orElseThrow(() -> new RuntimeException("Question not found with id: " + question.getId())));
                    break;
                case "SLIDER":
                    typedQuestions.add(questionSliderRepository.findById(question.getId())
                        .orElseThrow(() -> new RuntimeException("Question not found with id: " + question.getId())));
                    break;
                case "PUZZLE":
                    typedQuestions.add(questionPuzzleRepository.findById(question.getId())
                        .orElseThrow(() -> new RuntimeException("Question not found with id: " + question.getId())));
                    break;
                case "TEXT":
                    typedQuestions.add(questionTypeTextRepository.findById(question.getId())
                        .orElseThrow(() -> new RuntimeException("Question not found with id: " + question.getId())));
                    break;
                default:
                    typedQuestions.add(question);
            }
        }
        
        return typedQuestions;
    }

    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public List<Question> getQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    public List<Question> getLatestQuestions() {
        return questionRepository.findTop10ByOrderByCreatedAtDesc();
    }

    public Question updateQuestion(Long id, QuestionDTO questionDTO) {
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        
        QuestionType questionType = questionTypeRepository.findById(questionDTO.getQuestionTypeId())
                .orElseThrow(() -> new RuntimeException("Question type not found"));
        
        existingQuestion.setQuestionType(questionType);
        existingQuestion.setContent(questionDTO.getContent());
        existingQuestion.setImage(questionDTO.getImage());
        existingQuestion.setAudio(questionDTO.getAudio());
        existingQuestion.setDescription(questionDTO.getDescription());
        existingQuestion.setTimeLimit(questionDTO.getTimeLimit());
        existingQuestion.setUpdatedAt(LocalDateTime.now());
        
        return questionRepository.save(existingQuestion);
    }
    
    public QuestionTrueFalse updateTrueFalseQuestion(Long id, QuestionTrueFalseDTO dto) {
        QuestionTrueFalse existingQuestion = questionTrueFalseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("True/False question not found with id: " + id));
        
        existingQuestion.setContent(dto.getContent());
        existingQuestion.setImage(dto.getImage());
        existingQuestion.setAudio(dto.getAudio());
        existingQuestion.setDescription(dto.getDescription());
        existingQuestion.setTimeLimit(dto.getTimeLimit());
        existingQuestion.setPoint(dto.getPoint());
        existingQuestion.setCorrectAnswer(dto.getCorrectAnswer());
        existingQuestion.setUpdatedAt(LocalDateTime.now());
        
        return questionTrueFalseRepository.save(existingQuestion);
    }
    
    public QuestionChoice updateChoiceQuestion(Long id, QuestionChoiceDTO dto) {
        QuestionChoice existingQuestion = questionChoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Choice question not found with id: " + id));
        
        existingQuestion.setContent(dto.getContent());
        existingQuestion.setImage(dto.getImage());
        existingQuestion.setAudio(dto.getAudio());
        existingQuestion.setDescription(dto.getDescription());
        existingQuestion.setTimeLimit(dto.getTimeLimit());
        existingQuestion.setPoint(dto.getPoint());
        existingQuestion.setUpdatedAt(LocalDateTime.now());
        
        int correctCount = 0;
        for (ChoiceOptionDTO option : dto.getChoiceOptions()) {
            if (option.getIsCorrect()) {
                correctCount++;
            }
        }
        
        String questionTypeName = correctCount == 1 ? "SINGLE_CHOICE" : "MULTI_CHOICE";
        if (correctCount == 0 && !dto.getChoiceOptions().isEmpty()) {
            throw new RuntimeException("Choice questions must have at least one correct answer");
        }
        
        QuestionType questionType = questionTypeRepository.findByName(questionTypeName)
                .orElseThrow(() -> new RuntimeException("Question type " + questionTypeName + " not found"));
        existingQuestion.setQuestionType(questionType);
        
        existingQuestion.getChoiceOptions().clear();
        
        List<ChoiceOption> choiceOptions = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (ChoiceOptionDTO optionDTO : dto.getChoiceOptions()) {
            ChoiceOption option = ChoiceOption.builder()
                    .text(optionDTO.getText())
                    .image(optionDTO.getImage())
                    .audio(optionDTO.getAudio())
                    .isCorrect(optionDTO.getIsCorrect())
                    .createdAt(now)
                    .updatedAt(now)
                    .question(existingQuestion)
                    .build();
            choiceOptions.add(option);
        }
        
        existingQuestion.setChoiceOptions(choiceOptions);
        
        return questionChoiceRepository.save(existingQuestion);
    }
    
    public QuestionSlider updateSliderQuestion(Long id, QuestionSliderDTO dto) {
        QuestionSlider existingQuestion = questionSliderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slider question not found with id: " + id));
        
        existingQuestion.setContent(dto.getContent());
        existingQuestion.setImage(dto.getImage());
        existingQuestion.setAudio(dto.getAudio());
        existingQuestion.setDescription(dto.getDescription());
        existingQuestion.setTimeLimit(dto.getTimeLimit());
        existingQuestion.setPoint(dto.getPoint());
        existingQuestion.setMinValue(dto.getMinValue());
        existingQuestion.setMaxValue(dto.getMaxValue());
        existingQuestion.setDefaultValue(dto.getDefaultValue());
        existingQuestion.setCorrectAnswer(dto.getCorrectAnswer());
        existingQuestion.setLambda(dto.getLambda());
        existingQuestion.setUpdatedAt(LocalDateTime.now());
        
        return questionSliderRepository.save(existingQuestion);
    }
    
    public QuestionPuzzle updatePuzzleQuestion(Long id, QuestionPuzzleDTO dto) {
        QuestionPuzzle existingQuestion = questionPuzzleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Puzzle question not found with id: " + id));
        
        existingQuestion.setContent(dto.getContent());
        existingQuestion.setImage(dto.getImage());
        existingQuestion.setAudio(dto.getAudio());
        existingQuestion.setDescription(dto.getDescription());
        existingQuestion.setTimeLimit(dto.getTimeLimit());
        existingQuestion.setPoint(dto.getPoint());
        existingQuestion.setUpdatedAt(LocalDateTime.now());
        
        existingQuestion.getPuzzlePieces().clear();
        
        List<PuzzleOption> puzzlePieces = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (PuzzleOptionDTO optionDTO : dto.getPuzzlePieces()) {
            PuzzleOption option = PuzzleOption.builder()
                    .correctPosition(optionDTO.getCorrectPosition())
                    .text(optionDTO.getText())
                    .createdAt(now)
                    .updatedAt(now)
                    .question(existingQuestion)
                    .build();
            puzzlePieces.add(option);
        }
        
        existingQuestion.setPuzzlePieces(puzzlePieces);
        
        return questionPuzzleRepository.save(existingQuestion);
    }
    
    public QuestionTypeText updateTextQuestion(Long id, QuestionTypeTextDTO dto) {
        QuestionTypeText existingQuestion = questionTypeTextRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Text question not found with id: " + id));
        
        existingQuestion.setContent(dto.getContent());
        existingQuestion.setImage(dto.getImage());
        existingQuestion.setAudio(dto.getAudio());
        existingQuestion.setDescription(dto.getDescription());
        existingQuestion.setTimeLimit(dto.getTimeLimit());
        existingQuestion.setPoint(dto.getPoint());
        existingQuestion.setCaseSensitive(dto.getCaseSensitive());
        existingQuestion.setUpdatedAt(LocalDateTime.now());
        
        if (dto.getAcceptedAnswers() != null && !dto.getAcceptedAnswers().isEmpty()) {
            existingQuestion.getAcceptedAnswers().clear();
            
            List<TypeTextOption> acceptedAnswers = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();
            
            for (TypeTextOptionDTO optionDTO : dto.getAcceptedAnswers()) {
                TypeTextOption option = TypeTextOption.builder()
                        .text(optionDTO.getText())
                        .createdAt(now)
                        .updatedAt(now)
                        .question(existingQuestion)
                        .build();
                acceptedAnswers.add(option);
            }
            
            existingQuestion.setAcceptedAnswers(acceptedAnswers);
        }
        
        return questionTypeTextRepository.save(existingQuestion);
    }

    public boolean existsById(Long id) {
        return questionRepository.existsById(id);
    }
    
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}
