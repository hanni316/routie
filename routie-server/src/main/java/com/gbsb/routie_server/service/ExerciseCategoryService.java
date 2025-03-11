package com.gbsb.routie_server.service;

import com.gbsb.routie_server.entity.ExerciseCategory;
import com.gbsb.routie_server.repository.ExerciseCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseCategoryService {

    private final ExerciseCategoryRepository exerciseCategoryRepository;

    // 모든 운동 카테고리 조회
    public List<ExerciseCategory> getAllCategories() {
        return exerciseCategoryRepository.findAll();
    }
}