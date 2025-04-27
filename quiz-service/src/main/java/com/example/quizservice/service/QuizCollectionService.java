package com.example.quizservice.service;


import com.example.quizservice.dto.QuizCollectionDTO;
import com.example.quizservice.model.Quiz;
import com.example.quizservice.model.QuizCollection;
import com.example.quizservice.repository.QuizCollectionRepository;
import com.example.quizservice.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizCollectionService {
    private final QuizCollectionRepository quizCollectionRepository;
    private final QuizRepository quizRepository;

    public QuizCollection createQuizCollection(QuizCollectionDTO dto) {
        Quiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        QuizCollection quizCollection = QuizCollection.builder()
                .authorId(dto.getAuthorId())
                .quiz(quiz)
                .description(dto.getDescription())
                .category(dto.getCategory())
                .visibleTo(dto.getVisibleTo())
                .timestamp(LocalDateTime.now())
                .build();

        return quizCollectionRepository.save(quizCollection);
    }

    public List<QuizCollection> getAllQuizCollections() {
        return quizCollectionRepository.findAll();
    }

    public Optional<QuizCollection> getQuizCollectionById(Long id) {
        return quizCollectionRepository.findById(id);
    }

    public List<QuizCollection> getQuizCollectionsByAuthorId(Long authorId) {
        return quizCollectionRepository.findByAuthorId(authorId);
    }

    public List<QuizCollection> getQuizCollectionsByQuizId(Long quizId) {
        return quizCollectionRepository.findByQuizId(quizId);
    }

    public List<QuizCollection> getQuizCollectionsByCategory(String category) {
        return quizCollectionRepository.findByCategory(category);
    }

    public QuizCollection updateQuizCollection(Long id, QuizCollectionDTO dto) {
        QuizCollection quizCollection = quizCollectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz collection not found"));

        quizCollection.setDescription(dto.getDescription());
        quizCollection.setCategory(dto.getCategory());
        quizCollection.setVisibleTo(dto.getVisibleTo());

        return quizCollectionRepository.save(quizCollection);
    }

    public void deleteQuizCollection(Long id) {
        quizCollectionRepository.deleteById(id);
    }
}

