package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.entity.RoutineExercise;
import com.gbsb.routie_server.service.RoutineExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routine-exercises")
@RequiredArgsConstructor
public class RoutineExerciseController {
    private final RoutineExerciseService routineExerciseService;

    // 루틴에 운동 추가 (운동 ID, 운동 시간 입력)
    @PostMapping("/{routineId}/exercises/{exerciseId}")
    public ResponseEntity<RoutineExercise> addExerciseToRoutine(
            @PathVariable Long routineId,
            @PathVariable Long exerciseId,
            @RequestParam int duration) {

        RoutineExercise routineExercise = routineExerciseService.addExerciseToRoutine(routineId, exerciseId, duration);
        return ResponseEntity.ok(routineExercise);
    }

    // 특정 루틴의 운동 목록 조회
    @GetMapping("/{routineId}/exercises")
    public ResponseEntity<List<RoutineExercise>> getExercisesByRoutine(@PathVariable Long routineId) {
        List<RoutineExercise> exercises = routineExerciseService.getExercisesByRoutine(routineId);
        return ResponseEntity.ok(exercises);
    }

    // 루틴에서 특정 운동 삭제
    @DeleteMapping("/exercises/{routineExerciseId}")
    public ResponseEntity<String> removeExerciseFromRoutine(@PathVariable Long routineExerciseId) {
        routineExerciseService.removeExerciseFromRoutine(routineExerciseId);
        return ResponseEntity.ok("운동이 루틴에서 삭제되었습니다.");
    }
}
