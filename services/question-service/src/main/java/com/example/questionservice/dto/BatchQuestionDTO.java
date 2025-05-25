package com.example.questionservice.dto;

import lombok.Data;
import java.util.Map;

@Data
public class BatchQuestionDTO {
    private String questionType; 
    private String content;
    private String description;
    private Integer timeLimit;
    private Integer point;
    private String imageFileName; 
    private String audioFileName; 
    private Map<String, Object> data; 
    private Integer position; 
}