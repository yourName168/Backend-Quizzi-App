package com.example.questionservice.service;

import com.example.questionservice.model.QuestionType;
import com.example.questionservice.repository.QuestionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionTypeService {
    private final QuestionTypeRepository questionTypeRepository;

    public QuestionType createQuestionType(String name) {
        QuestionType questionType = QuestionType.builder()
                .name(name)
                .build();

        return questionTypeRepository.save(questionType);
    }

    public List<QuestionType> getAllQuestionTypes() {
        return questionTypeRepository.findAll();
    }

    public Optional<QuestionType> getQuestionTypeById(Long id) {
        return questionTypeRepository.findById(id);
    }

    public Optional<QuestionType> getQuestionTypeByName(String name) {
        return questionTypeRepository.findByName(name);
    }
}
