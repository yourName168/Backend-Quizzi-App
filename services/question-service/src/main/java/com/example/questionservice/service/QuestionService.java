package com.example.questionservice.service;

import com.example.questionservice.client.QuizClient;
import com.example.questionservice.dto.*;
import com.example.questionservice.model.*;
import com.example.questionservice.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
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
    private final FileStorageService fileStorageService;
    private final ObjectMapper objectMapper;


    public boolean existsById(Long id) {
        return questionRepository.existsById(id);
    }
    
    public void deleteQuestion(Long id) throws IOException {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
    
        if (question.getImage() != null && !question.getImage().isEmpty()) {
            fileStorageService.deleteFile(question.getImage());
        }
    
        if (question.getAudio() != null && !question.getAudio().isEmpty()) {
            fileStorageService.deleteFile(question.getAudio());
        }
    
        questionRepository.deleteById(id);
    }

    public boolean existsByQuizId(Long quizId) {
        return questionRepository.existsByQuizId(quizId);
    }

    public void deleteAllQuestionsByQuizId(Long quizId) throws IOException {
        List<Question> questions = questionRepository.findByQuizId(quizId);

        for (Question question : questions) {
            String questionType = question.getQuestionType().getName();

            if (question.getImage() != null && !question.getImage().isEmpty()) {
                fileStorageService.deleteFile(question.getImage());
            }
            if (question.getAudio() != null && !question.getAudio().isEmpty()) {
                fileStorageService.deleteFile(question.getAudio());
            }
            questionRepository.deleteById(question.getId());


            // switch (questionType) {
            //     case "TRUE_FALSE":
            //         questionTrueFalseRepository.deleteById(question.getId());
            //         break;
            //     case "SINGLE_CHOICE":
            //     case "MULTI_CHOICE":
            //         questionChoiceRepository.deleteById(question.getId());
            //         break;
            //     case "SLIDER":
            //         questionSliderRepository.deleteById(question.getId());
            //         break;
            //     case "PUZZLE":
            //         questionPuzzleRepository.deleteById(question.getId());
            //         break;
            //     case "TEXT":
            //         questionTypeTextRepository.deleteById(question.getId());
            //         break;
            //     default:
            //         questionRepository.deleteById(question.getId());
            // }
        }
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
        return questionRepository.findByQuizIdOrderByCreatedAtAsc(quizId);
    }

    public List<Question> getLatestQuestions() {
        return questionRepository.findTop10ByOrderByCreatedAtDesc();
    }

    private String processFileUpload(MultipartFile file, String fileType, String existingFilePath) throws IOException {
        if (file != null && !file.isEmpty()) {
            if (existingFilePath != null && !existingFilePath.isEmpty()) {
                fileStorageService.deleteFile(existingFilePath);
            }
    
            if ("image".equals(fileType)) {
                return fileStorageService.storeImageFile(file);
            } else if ("audio".equals(fileType)) {
                return fileStorageService.storeAudioFile(file);
            }
        }
    
        return existingFilePath;
    }
    
    private void validateFileUploads(MultipartFile imageFile, MultipartFile audioFile) {
        if (imageFile != null && !imageFile.isEmpty() && audioFile != null && !audioFile.isEmpty()) {
            throw new IllegalArgumentException("Only one of image or audio can be uploaded at a time");
        }
    }

    public Question createQuestion(QuestionDTO questionDTO) throws IOException {
        MultipartFile imageFile = questionDTO.getImageFile();
        MultipartFile audioFile = questionDTO.getAudioFile();
        validateFileUploads(imageFile, audioFile);
        
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

        String imagePath = processFileUpload(questionDTO.getImageFile(), "image", null);
        String audioPath = processFileUpload(questionDTO.getAudioFile(), "audio", null);

        Question question = Question.builder()
                .quizId(questionDTO.getQuizId())
                .questionType(questionType)
                .image(imagePath)
                .audio(audioPath)
                .content(questionDTO.getContent())
                .timeLimit(questionDTO.getTimeLimit())
                .point(questionDTO.getPoint())
                .description(questionDTO.getDescription())
                .position(questionDTO.getPosition()) // Add position
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return questionRepository.save(question);
    }

    public QuestionTrueFalse createTrueFalseQuestion(QuestionTrueFalseDTO dto) throws IOException {
        MultipartFile imageFile = dto.getImageFile();
        MultipartFile audioFile = dto.getAudioFile();
        validateFileUploads(imageFile, audioFile);
        
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

        String imagePath = processFileUpload(imageFile, "image", null);
        String audioPath = processFileUpload(audioFile, "audio", null);

        QuestionTrueFalse question = QuestionTrueFalse.builder()
                .quizId(dto.getQuizId())
                .questionType(questionType)
                .image(imagePath)
                .audio(audioPath)
                .content(dto.getContent())
                .timeLimit(dto.getTimeLimit())
                .point(dto.getPoint())
                .description(dto.getDescription())
                .position(dto.getPosition()) // Add position
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .correctAnswer(dto.getCorrectAnswer())
                .build();

        return questionTrueFalseRepository.save(question);
    }

    public QuestionChoice createChoiceQuestion(QuestionChoiceDTO dto) throws IOException {
        MultipartFile imageFile = dto.getImageFile();
        MultipartFile audioFile = dto.getAudioFile();
        validateFileUploads(imageFile, audioFile);
        
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
        
        String imagePath = processFileUpload(imageFile, "image", null);
        String audioPath = processFileUpload(audioFile, "audio", null);
        
        QuestionChoice question = QuestionChoice.builder()
                .quizId(dto.getQuizId())
                .questionType(questionType)
                .image(imagePath)
                .audio(audioPath)
                .content(dto.getContent())
                .timeLimit(dto.getTimeLimit())
                .point(dto.getPoint())
                .description(dto.getDescription())
                .position(dto.getPosition()) // Add position
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

    public QuestionSlider createSliderQuestion(QuestionSliderDTO dto) throws IOException {
        MultipartFile imageFile = dto.getImageFile();
        MultipartFile audioFile = dto.getAudioFile();
        validateFileUploads(imageFile, audioFile);
        
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

        String imagePath = processFileUpload(imageFile, "image", null);
        String audioPath = processFileUpload(audioFile, "audio", null);

        QuestionSlider question = QuestionSlider.builder()
                .quizId(dto.getQuizId())
                .questionType(questionType)
                .image(imagePath)
                .audio(audioPath)
                .content(dto.getContent())
                .timeLimit(dto.getTimeLimit())
                .point(dto.getPoint())
                .description(dto.getDescription())
                .position(dto.getPosition()) // Add position
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .minValue(dto.getMinValue())
                .maxValue(dto.getMaxValue())
                .defaultValue(dto.getDefaultValue())
                .correctAnswer(dto.getCorrectAnswer())
                .color(dto.getColor())
                .build();

        return questionSliderRepository.save(question);
    }

    public QuestionPuzzle createPuzzleQuestion(QuestionPuzzleDTO dto) throws IOException {
        MultipartFile imageFile = dto.getImageFile();
        MultipartFile audioFile = dto.getAudioFile();
        validateFileUploads(imageFile, audioFile);
        
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

        String imagePath = processFileUpload(imageFile, "image", null);
        String audioPath = processFileUpload(audioFile, "audio", null);

        QuestionPuzzle question = QuestionPuzzle.builder()
                .quizId(dto.getQuizId())
                .questionType(questionType)
                .image(imagePath)
                .audio(audioPath)
                .content(dto.getContent())
                .timeLimit(dto.getTimeLimit())
                .point(dto.getPoint())
                .description(dto.getDescription())
                .position(dto.getPosition()) // Add position
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<PuzzleOption> puzzleOptions = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (PuzzleOptionDTO optionDTO : dto.getPuzzlePieces()) {
            PuzzleOption option = PuzzleOption.builder()
                    .correctPosition(optionDTO.getCorrectPosition())
                    .text(optionDTO.getText())
                    .createdAt(now)
                    .updatedAt(now)
                    .question(question)
                    .build();
            puzzleOptions.add(option);
        }

        question.setPuzzlePieces(puzzleOptions);

        return questionPuzzleRepository.save(question);
    }

    public QuestionTypeText createTextQuestion(QuestionTypeTextDTO dto) throws IOException {
        MultipartFile imageFile = dto.getImageFile();
        MultipartFile audioFile = dto.getAudioFile();
        validateFileUploads(imageFile, audioFile);
        
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

        String imagePath = processFileUpload(imageFile, "image", null);
        String audioPath = processFileUpload(audioFile, "audio", null);

        QuestionTypeText question = QuestionTypeText.builder()
                .quizId(dto.getQuizId())
                .questionType(questionType)
                .image(imagePath)
                .audio(audioPath)
                .content(dto.getContent())
                .timeLimit(dto.getTimeLimit())
                .point(dto.getPoint())
                .description(dto.getDescription())
                .position(dto.getPosition()) // Add position
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .caseSensitive(dto.getCaseSensitive())
                .build();

        if (dto.getAcceptedAnswers() != null && !dto.getAcceptedAnswers().isEmpty()) {
            List<TypeTextOption> acceptedAnswers = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();
            
            for (TypeTextOptionDTO optionDTO : dto.getAcceptedAnswers()) {
                TypeTextOption option = TypeTextOption.builder()
                        .text(optionDTO.getText())
                        .createdAt(now)
                        .updatedAt(now)
                        .question(question)
                        .build();
                acceptedAnswers.add(option);
            }
            
            question.setAcceptedAnswers(acceptedAnswers);
        }

        return questionTypeTextRepository.save(question);
    }


    public Question updateQuestion(Long id, QuestionDTO questionDTO) throws IOException {
        MultipartFile imageFile = questionDTO.getImageFile();
        MultipartFile audioFile = questionDTO.getAudioFile();
        validateFileUploads(imageFile, audioFile);
        
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        
        QuestionType questionType = questionTypeRepository.findById(questionDTO.getQuestionTypeId())
                .orElseThrow(() -> new RuntimeException("Question type not found"));
        
        String imagePath = processFileUpload(imageFile, "image", existingQuestion.getImage());
        String audioPath = processFileUpload(audioFile, "audio", existingQuestion.getAudio());
        
        existingQuestion.setQuestionType(questionType);
        existingQuestion.setContent(questionDTO.getContent());
        existingQuestion.setImage(imagePath);
        existingQuestion.setAudio(audioPath);
        existingQuestion.setDescription(questionDTO.getDescription());
        existingQuestion.setTimeLimit(questionDTO.getTimeLimit());
        existingQuestion.setPoint(questionDTO.getPoint());
        if (questionDTO.getPosition() != 0) { // Assuming 0 is the default value
            existingQuestion.setPosition(questionDTO.getPosition());
        }
        existingQuestion.setUpdatedAt(LocalDateTime.now());
        
        return questionRepository.save(existingQuestion);
    }
    
    public QuestionTrueFalse updateTrueFalseQuestion(Long id, QuestionTrueFalseDTO dto) throws IOException {
        MultipartFile imageFile = dto.getImageFile();
        MultipartFile audioFile = dto.getAudioFile();
        validateFileUploads(imageFile, audioFile);
        
        QuestionTrueFalse existingQuestion = questionTrueFalseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("True/False question not found with id: " + id));
        
        String imagePath = processFileUpload(imageFile, "image", existingQuestion.getImage());
        String audioPath = processFileUpload(audioFile, "audio", existingQuestion.getAudio());
        
        existingQuestion.setContent(dto.getContent());
        existingQuestion.setImage(imagePath);
        existingQuestion.setAudio(audioPath);
        existingQuestion.setDescription(dto.getDescription());
        existingQuestion.setTimeLimit(dto.getTimeLimit());
        existingQuestion.setPoint(dto.getPoint());
        existingQuestion.setCorrectAnswer(dto.getCorrectAnswer());
        if (dto.getPosition() != 0) {
            existingQuestion.setPosition(dto.getPosition());
        }
        existingQuestion.setUpdatedAt(LocalDateTime.now());
        
        return questionTrueFalseRepository.save(existingQuestion);
    }
    
    public QuestionChoice updateChoiceQuestion(Long id, QuestionChoiceDTO dto) throws IOException {
        MultipartFile imageFile = dto.getImageFile();
        MultipartFile audioFile = dto.getAudioFile();
        validateFileUploads(imageFile, audioFile);
        
        QuestionChoice existingQuestion = questionChoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Choice question not found with id: " + id));
        
        String imagePath = processFileUpload(imageFile, "image", existingQuestion.getImage());
        String audioPath = processFileUpload(audioFile, "audio", existingQuestion.getAudio());
        
        existingQuestion.setContent(dto.getContent());
        existingQuestion.setImage(imagePath);
        existingQuestion.setAudio(audioPath);
        existingQuestion.setDescription(dto.getDescription());
        existingQuestion.setTimeLimit(dto.getTimeLimit());
        existingQuestion.setPoint(dto.getPoint());
        if (dto.getPosition() != 0) {
            existingQuestion.setPosition(dto.getPosition());
        }
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
    
    public QuestionSlider updateSliderQuestion(Long id, QuestionSliderDTO dto) throws IOException {
        MultipartFile imageFile = dto.getImageFile();
        MultipartFile audioFile = dto.getAudioFile();
        validateFileUploads(imageFile, audioFile);
        
        QuestionSlider existingQuestion = questionSliderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slider question not found with id: " + id));
        
        String imagePath = processFileUpload(imageFile, "image", existingQuestion.getImage());
        String audioPath = processFileUpload(audioFile, "audio", existingQuestion.getAudio());
        
        existingQuestion.setContent(dto.getContent());
        existingQuestion.setImage(imagePath);
        existingQuestion.setAudio(audioPath);
        existingQuestion.setDescription(dto.getDescription());
        existingQuestion.setTimeLimit(dto.getTimeLimit());
        existingQuestion.setPoint(dto.getPoint());
        existingQuestion.setMinValue(dto.getMinValue());
        existingQuestion.setMaxValue(dto.getMaxValue());
        existingQuestion.setDefaultValue(dto.getDefaultValue());
        existingQuestion.setCorrectAnswer(dto.getCorrectAnswer());
        existingQuestion.setColor(dto.getColor());
        if (dto.getPosition() != 0) {
            existingQuestion.setPosition(dto.getPosition());
        }
        existingQuestion.setUpdatedAt(LocalDateTime.now());
        
        return questionSliderRepository.save(existingQuestion);
    }
    
    public QuestionPuzzle updatePuzzleQuestion(Long id, QuestionPuzzleDTO dto) throws IOException {
        MultipartFile imageFile = dto.getImageFile();
        MultipartFile audioFile = dto.getAudioFile();
        validateFileUploads(imageFile, audioFile);
        
        QuestionPuzzle existingQuestion = questionPuzzleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Puzzle question not found with id: " + id));
        
        String imagePath = processFileUpload(imageFile, "image", existingQuestion.getImage());
        String audioPath = processFileUpload(audioFile, "audio", existingQuestion.getAudio());
        
        existingQuestion.setContent(dto.getContent());
        existingQuestion.setImage(imagePath);
        existingQuestion.setAudio(audioPath);
        existingQuestion.setDescription(dto.getDescription());
        existingQuestion.setTimeLimit(dto.getTimeLimit());
        existingQuestion.setPoint(dto.getPoint());
        if (dto.getPosition() != 0) {
            existingQuestion.setPosition(dto.getPosition());
        }
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
    
    public QuestionTypeText updateTextQuestion(Long id, QuestionTypeTextDTO dto) throws IOException {
        MultipartFile imageFile = dto.getImageFile();
        MultipartFile audioFile = dto.getAudioFile();
        validateFileUploads(imageFile, audioFile);
        
        QuestionTypeText existingQuestion = questionTypeTextRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Text question not found with id: " + id));
        
        String imagePath = processFileUpload(imageFile, "image", existingQuestion.getImage());
        String audioPath = processFileUpload(audioFile, "audio", existingQuestion.getAudio());
        
        existingQuestion.setContent(dto.getContent());
        existingQuestion.setImage(imagePath);
        existingQuestion.setAudio(audioPath);
        existingQuestion.setDescription(dto.getDescription());
        existingQuestion.setTimeLimit(dto.getTimeLimit());
        existingQuestion.setPoint(dto.getPoint());
        existingQuestion.setCaseSensitive(dto.getCaseSensitive());
        if (dto.getPosition() != 0) {
            existingQuestion.setPosition(dto.getPosition());
        }
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
    
    
    @Transactional
    public List<Question> createQuestionsBatch(Long quizId, String questionsJson, List<MultipartFile> files) throws IOException {
        try {
            Object quiz = quizClient.getQuizById(quizId);
            if (quiz == null) {
                throw new RuntimeException("Quiz not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error checking quiz: " + e.getMessage());
        }
        
        List<BatchQuestionDTO> batchQuestionDTOs = objectMapper.readValue(
            questionsJson, new TypeReference<List<BatchQuestionDTO>>() {});
        
        Map<String, MultipartFile> fileMap = new HashMap<>();
        if (files != null) {
            for (MultipartFile file : files) {
                String fileKey = file.getOriginalFilename();
                fileMap.put(fileKey, file);
            }
        }
        
        List<Question> createdQuestions = new ArrayList<>();
        
        for (BatchQuestionDTO batchDTO : batchQuestionDTOs) {
            String type = batchDTO.getQuestionType();
            MultipartFile imageFile = null;
            MultipartFile audioFile = null;
            
            if (batchDTO.getImageFileName() != null && !batchDTO.getImageFileName().isEmpty()) {
                imageFile = fileMap.get(batchDTO.getImageFileName());
            }
            
            if (batchDTO.getAudioFileName() != null && !batchDTO.getAudioFileName().isEmpty()) {
                audioFile = fileMap.get(batchDTO.getAudioFileName());
            }
            
            Question createdQuestion;
            
            switch (type) {
                case "TRUE_FALSE":
                    QuestionTrueFalseDTO tfDTO = convertToTrueFalseDTO(batchDTO, quizId, imageFile, audioFile);
                    createdQuestion = createTrueFalseQuestion(tfDTO);
                    break;
                case "SINGLE_CHOICE":
                case "MULTI_CHOICE":
                    QuestionChoiceDTO choiceDTO = convertToChoiceDTO(batchDTO, quizId, imageFile, audioFile);
                    createdQuestion = createChoiceQuestion(choiceDTO);
                    break;
                case "SLIDER":
                    QuestionSliderDTO sliderDTO = convertToSliderDTO(batchDTO, quizId, imageFile, audioFile);
                    createdQuestion = createSliderQuestion(sliderDTO);
                    break;
                case "PUZZLE":
                    QuestionPuzzleDTO puzzleDTO = convertToPuzzleDTO(batchDTO, quizId, imageFile, audioFile);
                    createdQuestion = createPuzzleQuestion(puzzleDTO);
                    break;
                case "TEXT":
                    QuestionTypeTextDTO textDTO = convertToTextDTO(batchDTO, quizId, imageFile, audioFile);
                    createdQuestion = createTextQuestion(textDTO);
                    break;
                default:
                    QuestionDTO genericDTO = convertToGenericDTO(batchDTO, quizId, imageFile, audioFile);
                    createdQuestion = createQuestion(genericDTO);
            }
            
            createdQuestions.add(createdQuestion);
        }
        
        return createdQuestions;
    }
    
    
    private QuestionTrueFalseDTO convertToTrueFalseDTO(BatchQuestionDTO batchDTO, Long quizId, 
                                                     MultipartFile imageFile, MultipartFile audioFile) {
        QuestionTrueFalseDTO dto = new QuestionTrueFalseDTO();
        dto.setQuizId(quizId);
        dto.setContent(batchDTO.getContent());
        dto.setDescription(batchDTO.getDescription());
        dto.setTimeLimit(batchDTO.getTimeLimit());
        dto.setPoint(batchDTO.getPoint());
        dto.setPosition(batchDTO.getPosition()); // Add position
        dto.setImageFile(imageFile);
        dto.setAudioFile(audioFile);
        dto.setCorrectAnswer(Boolean.valueOf(String.valueOf(batchDTO.getData().get("correctAnswer"))));

        return dto;
    }
    
    private QuestionChoiceDTO convertToChoiceDTO(BatchQuestionDTO batchDTO, Long quizId, 
                                               MultipartFile imageFile, MultipartFile audioFile) {
        QuestionChoiceDTO dto = new QuestionChoiceDTO();
        dto.setQuizId(quizId);
        dto.setContent(batchDTO.getContent());
        dto.setDescription(batchDTO.getDescription());
        dto.setTimeLimit(batchDTO.getTimeLimit());
        dto.setPoint(batchDTO.getPoint());
        dto.setPosition(batchDTO.getPosition()); // Add position
        dto.setImageFile(imageFile);
        dto.setAudioFile(audioFile);
        
        List<ChoiceOptionDTO> options = new ArrayList<>();
        List<Map<String, Object>> optionsData = (List<Map<String, Object>>) batchDTO.getData().get("choiceOptions");
        
        if (optionsData != null) {
            for (Map<String, Object> optionData : optionsData) {
                ChoiceOptionDTO optionDTO = new ChoiceOptionDTO();
                optionDTO.setText((String) optionData.get("text"));
                optionDTO.setImage((String) optionData.get("image"));
                optionDTO.setAudio((String) optionData.get("audio"));
                optionDTO.setIsCorrect((Boolean) optionData.get("isCorrect"));
                options.add(optionDTO);
            }
            }
        
        dto.setChoiceOptions(options);
        return dto;
    }
    
    private QuestionSliderDTO convertToSliderDTO(BatchQuestionDTO batchDTO, Long quizId, 
                                               MultipartFile imageFile, MultipartFile audioFile) {
        QuestionSliderDTO dto = new QuestionSliderDTO();
        dto.setQuizId(quizId);
        dto.setContent(batchDTO.getContent());
        dto.setDescription(batchDTO.getDescription());
        dto.setTimeLimit(batchDTO.getTimeLimit());
        dto.setPoint(batchDTO.getPoint());
        dto.setPosition(batchDTO.getPosition()); // Add position
        dto.setImageFile(imageFile);
        dto.setAudioFile(audioFile);

        Object minValueObj = batchDTO.getData().get("minValue");
        Object maxValueObj = batchDTO.getData().get("maxValue");
        Object defaultValueObj = batchDTO.getData().get("defaultValue");
        Object correctAnswerObj = batchDTO.getData().get("correctAnswer");
        Object colorObj = batchDTO.getData().get("color");

        dto.setMinValue(minValueObj != null ? Integer.parseInt(minValueObj.toString()) : 0);
        dto.setMaxValue(maxValueObj != null ? Integer.parseInt(maxValueObj.toString()) : 100);
        dto.setDefaultValue(defaultValueObj != null ? Integer.parseInt(defaultValueObj.toString()) : 50);
        dto.setCorrectAnswer(correctAnswerObj != null ? Integer.parseInt(correctAnswerObj.toString()) : 50);
        dto.setColor(colorObj != null ? colorObj.toString() : null);
        
        return dto;
    }
    
    private QuestionPuzzleDTO convertToPuzzleDTO(BatchQuestionDTO batchDTO, Long quizId, 
                                               MultipartFile imageFile, MultipartFile audioFile) {
        QuestionPuzzleDTO dto = new QuestionPuzzleDTO();
        dto.setQuizId(quizId);
        dto.setContent(batchDTO.getContent());
        dto.setDescription(batchDTO.getDescription());
        dto.setTimeLimit(batchDTO.getTimeLimit());
        dto.setPoint(batchDTO.getPoint());
        dto.setPosition(batchDTO.getPosition()); // Add position
        dto.setImageFile(imageFile);
        dto.setAudioFile(audioFile);
        
        List<PuzzleOptionDTO> pieces = new ArrayList<>();
        List<Map<String, Object>> piecesData = (List<Map<String, Object>>) batchDTO.getData().get("puzzlePieces");
        
        if (piecesData != null) {
            for (Map<String, Object> pieceData : piecesData) {
                PuzzleOptionDTO pieceDTO = new PuzzleOptionDTO();
                pieceDTO.setText((String) pieceData.get("text"));
                pieceDTO.setCorrectPosition(Integer.parseInt(pieceData.get("correctPosition").toString()));
                pieces.add(pieceDTO);
            }
        }
        
        dto.setPuzzlePieces(pieces);
        return dto;
    }
    
    private QuestionTypeTextDTO convertToTextDTO(BatchQuestionDTO batchDTO, Long quizId, 
                                               MultipartFile imageFile, MultipartFile audioFile) {
        QuestionTypeTextDTO dto = new QuestionTypeTextDTO();
        dto.setQuizId(quizId);
        dto.setContent(batchDTO.getContent());
        dto.setDescription(batchDTO.getDescription());
        dto.setTimeLimit(batchDTO.getTimeLimit());
        dto.setPoint(batchDTO.getPoint());
        dto.setPosition(batchDTO.getPosition()); // Add position
        dto.setImageFile(imageFile);
        dto.setAudioFile(audioFile);
        
        Object caseSensitiveObj = batchDTO.getData().get("caseSensitive");
        dto.setCaseSensitive(caseSensitiveObj != null ? Boolean.parseBoolean(caseSensitiveObj.toString()) : false);
        
        List<TypeTextOptionDTO> acceptedAnswers = new ArrayList<>();
        List<Map<String, Object>> answersData = (List<Map<String, Object>>) batchDTO.getData().get("acceptedAnswers");
        
        if (answersData != null) {
            for (Map<String, Object> answerData : answersData) {
                TypeTextOptionDTO answerDTO = new TypeTextOptionDTO();
                answerDTO.setText((String) answerData.get("text"));
                acceptedAnswers.add(answerDTO);
            }
        }
        
        dto.setAcceptedAnswers(acceptedAnswers);
        return dto;
    }
    
    private QuestionDTO convertToGenericDTO(BatchQuestionDTO batchDTO, Long quizId, 
                                          MultipartFile imageFile, MultipartFile audioFile) {
        QuestionDTO dto = new QuestionDTO();
        dto.setQuizId(quizId);
        
        Object questionTypeIdObj = batchDTO.getData().get("questionTypeId");
        if (questionTypeIdObj != null) {
            dto.setQuestionTypeId(Long.parseLong(questionTypeIdObj.toString()));
        }
        
        dto.setContent(batchDTO.getContent());
        dto.setDescription(batchDTO.getDescription());
        dto.setTimeLimit(batchDTO.getTimeLimit());
        dto.setPoint(batchDTO.getPoint());
        dto.setPosition(batchDTO.getPosition()); 
        dto.setImageFile(imageFile);
        dto.setAudioFile(audioFile);
        return dto;
    }

    // @Transactional
    // public List<Question> updateQuestionPositions(List<QuestionDTO> positionUpdates) {
    //     List<Question> updatedQuestions = new ArrayList<>();
        
    //     for (QuestionDTO update : positionUpdates) {
    //         Question question = questionRepository.findById(update.getId())
    //                 .orElseThrow(() -> new RuntimeException("Question not found with id: " + update.getId()));
            
    //         question.setPosition(update.getPosition());
    //         question.setUpdatedAt(LocalDateTime.now());
    //         updatedQuestions.add(questionRepository.save(question));
    //     }
        
    //     return updatedQuestions;
    // }
    
    public List<Question> getQuestionsByQuizIdOrderedByPosition(Long quizId) {
        return questionRepository.findByQuizIdOrderByPositionAsc(quizId);
    }
}