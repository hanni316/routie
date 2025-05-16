package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.ExerciseCategoryDto;
import com.gbsb.routie_server.entity.ExerciseCategory;
import com.gbsb.routie_server.repository.ExerciseCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseCategoryService {

    private final ExerciseCategoryRepository categoryRepository;

    public List<ExerciseCategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(entity -> ExerciseCategoryDto.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .build()
                )
                .collect(Collectors.toList());
    }
    public ExerciseCategoryDto getCategoryByName(String name) {
        ExerciseCategory entity = categoryRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리: " + name));
        return ExerciseCategoryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
