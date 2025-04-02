package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.entity.RoutineExercise;
import com.gbsb.routie_server.service.RoutineExerciseService;
import com.gbsb.routie_server.dto.ExerciseResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/routine-exercises")
@RequiredArgsConstructor
public class RoutineExerciseController {
    private final RoutineExerciseService routineExerciseService;

    // 특정 루틴에 운동 추가
    @PostMapping("/{routineId}/exercises/{exerciseId}")
    public ResponseEntity<RoutineExercise> addExerciseToRoutine(
            @PathVariable Long routineId,
            @PathVariable Long exerciseId) {
        RoutineExercise routineExercise = routineExerciseService.addExerciseToRoutine(routineId, exerciseId);
        return ResponseEntity.ok(routineExercise);
    }

    // 특정 루틴의 운동 목록 조회
    /*@GetMapping("/{routineId}/exercises")
    public ResponseEntity<List<RoutineExercise>> getExercisesByRoutine(@PathVariable Long routineId) {
        List<RoutineExercise> exercises = routineExerciseService.getExercisesByRoutine(routineId);
        return ResponseEntity.ok(exercises);
    }*/
    @GetMapping("/{routineId}/exercises")
    public ResponseEntity<List<ExerciseResponseDto>> getExercisesByRoutine(@PathVariable Long routineId) {
        return ResponseEntity.ok(routineExerciseService.getExercisesByRoutine(routineId));
    }

    // 특정 루틴에서 운동 삭제
    @DeleteMapping("/exercises/{routineExerciseId}")
    public ResponseEntity<Map<String, String>> removeExerciseFromRoutine(@PathVariable Long routineExerciseId) {
        routineExerciseService.removeExerciseFromRoutine(routineExerciseId);
        Map<String, String> result = new HashMap<>();
        result.put("message", "운동이 루틴에서 삭제되었습니다.");
        return ResponseEntity.ok(result);
    }
}