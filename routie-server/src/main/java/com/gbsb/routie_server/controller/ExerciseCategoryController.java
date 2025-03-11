package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.entity.ExerciseCategory;
import com.gbsb.routie_server.service.ExerciseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercise-categories")
@RequiredArgsConstructor
public class ExerciseCategoryController {

    private final ExerciseCategoryService exerciseCategoryService;

    // 모든 운동 카테고리 조회
    @GetMapping
    public ResponseEntity<List<ExerciseCategory>> getAllCategories() {
        List<ExerciseCategory> categories = exerciseCategoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}