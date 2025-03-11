package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.entity.Exercise;
import com.gbsb.routie_server.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    // 모든 운동 목록 조회
    @GetMapping
    public ResponseEntity<List<Exercise>> getAllExercises() {
        List<Exercise> exercises = exerciseService.getAllExercises();
        return ResponseEntity.ok(exercises);
    }

    // 특정 카테고리에 속한 운동 조회
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Exercise>> getExercisesByCategory(@PathVariable Long categoryId) {
        List<Exercise> exercises = exerciseService.getExercisesByCategory(categoryId);
        return ResponseEntity.ok(exercises);
    }
}