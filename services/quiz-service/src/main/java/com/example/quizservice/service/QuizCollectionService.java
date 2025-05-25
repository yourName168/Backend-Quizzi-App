package com.example.quizservice.service;

import com.example.quizservice.dto.QuizCollectionDTO;
import com.example.quizservice.model.Quiz;
import com.example.quizservice.model.QuizCollection;
import com.example.quizservice.repository.QuizCollectionRepository;
import com.example.quizservice.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizCollectionService {
    private final QuizCollectionRepository quizCollectionRepository;
    private final QuizRepository quizRepository;
    private final FileStorageService fileStorageService;

    public List<QuizCollection> getAllQuizCollections() {
        return quizCollectionRepository.findAllWithQuizzes();
    }

    public Optional<QuizCollection> getQuizCollectionById(Long id) {
        return quizCollectionRepository.findByIdWithQuizzes(id);
    }

    public List<QuizCollection> getQuizCollectionsByAuthorId(Long authorId) {
        return quizCollectionRepository.findByAuthorIdWithQuizzes(authorId);
    }

    public List<QuizCollection> getQuizCollectionsByCategory(String category) {
        return quizCollectionRepository.findByCategoryWithQuizzes(category);
    }

    public QuizCollection createQuizCollection(QuizCollectionDTO collectionDTO, MultipartFile coverPhotoFile) throws IOException {
        QuizCollection collection = convertToEntity(collectionDTO);
        
        if (coverPhotoFile != null && !coverPhotoFile.isEmpty()) {
            String photoPath = fileStorageService.storeCollectionCoverPhoto(coverPhotoFile);
            collection.setCoverPhoto(photoPath);
        } else if (collectionDTO.getCoverPhoto() != null) {
            collection.setCoverPhoto(collectionDTO.getCoverPhoto());
        }
        
        collection.setTimestamp(LocalDateTime.now());
        
        return quizCollectionRepository.save(collection);
    }
    
    public QuizCollection updateQuizCollection(Long id, QuizCollectionDTO collectionDTO, MultipartFile coverPhotoFile) throws IOException {
        QuizCollection existingCollection = quizCollectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found with id: " + id));
        
        existingCollection.setDescription(collectionDTO.getDescription());
        existingCollection.setCategory(collectionDTO.getCategory());
        existingCollection.setVisibleTo(collectionDTO.getVisibleTo());
        
        // Handle cover photo
        if (coverPhotoFile != null && !coverPhotoFile.isEmpty()) {
            if (existingCollection.getCoverPhoto() != null) {
                fileStorageService.deleteFile(existingCollection.getCoverPhoto());
            }
            
            String photoPath = fileStorageService.storeCollectionCoverPhoto(coverPhotoFile);
            existingCollection.setCoverPhoto(photoPath);
        } else if (collectionDTO.getCoverPhoto() != null && !collectionDTO.getCoverPhoto().equals(existingCollection.getCoverPhoto())) {
            // If a new coverPhoto URL is provided in the DTO but no file is uploaded
            existingCollection.setCoverPhoto(collectionDTO.getCoverPhoto());
        }
        
        existingCollection.setTimestamp(LocalDateTime.now());
        
        return quizCollectionRepository.save(existingCollection);
    }
    
    public void deleteQuizCollection(Long id) throws IOException {
        QuizCollection collection = quizCollectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found with id: " + id));
        
        if (collection.getCoverPhoto() != null) {
            fileStorageService.deleteFile(collection.getCoverPhoto());
        }
        
        quizCollectionRepository.delete(collection);
    }
    
    /**
     * Adds a quiz to a collection
     * @param collectionId ID of the collection
     * @param quizId ID of the quiz to add
     * @return The updated QuizCollection
     */
    public QuizCollection addQuizToCollection(Long collectionId, Long quizId) {
        QuizCollection collection = quizCollectionRepository.findByIdWithQuizzes(collectionId)
                .orElseThrow(() -> new RuntimeException("Collection not found with id: " + collectionId));
        
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
                
        quiz.setQuizCollection(collection);
        quizRepository.save(quiz);
        
        // The collection's quizzes set should be automatically updated due to the bidirectional relationship
        return quizCollectionRepository.findByIdWithQuizzes(collectionId).orElseThrow();
    }
      /**
     * Removes a quiz from a collection
     * @param collectionId ID of the collection
     * @param quizId ID of the quiz to remove
     * @return The updated QuizCollection
     */
    public QuizCollection removeQuizFromCollection(Long collectionId, Long quizId) {
        // Verify collection exists
        quizCollectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Collection not found with id: " + collectionId));
        
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
                
        quiz.setQuizCollection(null);
        quizRepository.save(quiz);
        
        return quizCollectionRepository.findByIdWithQuizzes(collectionId).orElseThrow();
    }
    
    private QuizCollection convertToEntity(QuizCollectionDTO dto) {
        QuizCollection collection = new QuizCollection();
        collection.setAuthorId(dto.getAuthorId());
        collection.setDescription(dto.getDescription());
        collection.setCategory(dto.getCategory());
        collection.setVisibleTo(dto.getVisibleTo());
        collection.setCoverPhoto(dto.getCoverPhoto());
        return collection;
    }
}