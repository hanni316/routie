package com.gbsb.routie_server.service;

import com.gbsb.routie_server.entity.Exercise;
import com.gbsb.routie_server.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    // 모든 운동 목록 조회
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    // 특정 카테고리에 속한 운동 목록 조회
    public List<Exercise> getExercisesByCategory(Long categoryId) {
        return exerciseRepository.findByCategory_Id(categoryId);
    }
}
