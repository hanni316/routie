package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.ExerciseCategoryDto;
import com.gbsb.routie_server.service.ExerciseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercise-categories")
@RequiredArgsConstructor
public class ExerciseCategoryController {

    private final ExerciseCategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<ExerciseCategoryDto>> getAllCategories() {
        List<ExerciseCategoryDto> list = categoryService.getAllCategories();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<ExerciseCategoryDto> getCategoryByName(
            @PathVariable String name) {
        ExerciseCategoryDto dto = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(dto);
    }
}
