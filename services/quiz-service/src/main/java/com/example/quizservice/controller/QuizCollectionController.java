package com.example.quizservice.controller;

import com.example.quizservice.dto.QuizCollectionDTO;
import com.example.quizservice.model.Quiz;
import com.example.quizservice.model.QuizCollection;
import com.example.quizservice.service.QuizCollectionService;
import com.example.quizservice.service.QuizService;
import com.example.quizservice.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/quiz-collections")
@RequiredArgsConstructor
public class QuizCollectionController {
    private final QuizCollectionService quizCollectionService;
    private final QuizService quizService;
    private final UrlService urlService;

    @GetMapping
    public ResponseEntity<List<QuizCollection>> getAllQuizCollections(HttpServletRequest request) {
        List<QuizCollection> collections = quizCollectionService.getAllQuizCollections();
        collections.forEach(collection -> {
            enrichWithCompleteUrl(collection, request);
            addQuizWithCompleteUrl(collection, request);
        });
        return ResponseEntity.ok(collections);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizCollection> getQuizCollectionById(@PathVariable Long id, HttpServletRequest request) {
        return quizCollectionService.getQuizCollectionById(id)
                .map(collection -> {
                    enrichWithCompleteUrl(collection, request);
                    addQuizWithCompleteUrl(collection, request); 
                    return ResponseEntity.ok(collection);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<QuizCollection>> getQuizCollectionsByAuthorId(@PathVariable Long authorId, HttpServletRequest request) {
        List<QuizCollection> collections = quizCollectionService.getQuizCollectionsByAuthorId(authorId);
        collections.forEach(collection -> enrichWithCompleteUrl(collection, request));
        return ResponseEntity.ok(collections);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<QuizCollection>> getQuizCollectionsByCategory(@PathVariable String category, HttpServletRequest request) {
        List<QuizCollection> collections = quizCollectionService.getQuizCollectionsByCategory(category);
        collections.forEach(collection -> enrichWithCompleteUrl(collection, request));
        return ResponseEntity.ok(collections);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<QuizCollection> createQuizCollection(@ModelAttribute QuizCollectionDTO collectionDTO, HttpServletRequest request) {
        try {
            QuizCollection createdCollection = quizCollectionService.createQuizCollection(
                collectionDTO, collectionDTO.getCoverPhotoFile());
            enrichWithCompleteUrl(createdCollection, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCollection);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<QuizCollection> updateQuizCollection(
            @PathVariable Long id,
            @ModelAttribute QuizCollectionDTO collectionDTO, HttpServletRequest request) {
        try {
            QuizCollection updatedCollection = quizCollectionService.updateQuizCollection(
                id, collectionDTO, collectionDTO.getCoverPhotoFile());
            enrichWithCompleteUrl(updatedCollection, request);
            return ResponseEntity.ok(updatedCollection);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizCollection(@PathVariable Long id) {
        try {
            quizCollectionService.deleteQuizCollection(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public void enrichWithCompleteUrl(QuizCollection collection,  HttpServletRequest request) {
        if (collection.getCoverPhoto() != null) {
            collection.setCoverPhoto(urlService.getCompleteFileUrl(collection.getCoverPhoto(), request));
        }
    }

    public void addQuizWithCompleteUrl(QuizCollection collection, HttpServletRequest request) {
        Set<Quiz> quizzes = quizService.getQuizzesByCollection(collection);
        quizzes.forEach(quiz -> {
            if (quiz.getCoverPhoto() != null) {
                quiz.setCoverPhoto(urlService.getCompleteFileUrl(quiz.getCoverPhoto(), request));
            }
        });
        collection.setQuizzes(quizzes);
    }
}
