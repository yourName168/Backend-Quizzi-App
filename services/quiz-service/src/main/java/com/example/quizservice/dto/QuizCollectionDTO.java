package com.example.quizservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizCollectionDTO {
    private Long id;
    private Long authorId;
    private String description;
    private String category;
    private Boolean visibleTo;
    private String coverPhoto;
    
    private transient MultipartFile coverPhotoFile;
}