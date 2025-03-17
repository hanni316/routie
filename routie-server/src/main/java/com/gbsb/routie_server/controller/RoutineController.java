package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.entity.Routine;
import com.gbsb.routie_server.dto.RoutineRequestDto;
import com.gbsb.routie_server.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routines")
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;
    // 운동 루틴 생성
    @PostMapping("/{userId}")
    public ResponseEntity<Routine> createRoutineWithExercises(
            @PathVariable String userId,
            @RequestBody RoutineRequestDto routineRequestDto) {

        Routine createdRoutine = routineService.createRoutineWithExercises(userId, routineRequestDto);
        return ResponseEntity.ok(createdRoutine);
    }

    // 특정 사용자의 루틴 목록 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<Routine>> getUserRoutines(@PathVariable String userId) {
        List<Routine> routines = routineService.getUserRoutines(userId);
        return ResponseEntity.ok(routines);
    }

    // 운동 루틴 수정
    @PutMapping("/{routineId}")
    public ResponseEntity<Routine> updateRoutine(@PathVariable Long routineId, @RequestBody Routine routine) {
        Routine updatedRoutine = routineService.updateRoutine(routineId, routine);
        return ResponseEntity.ok(updatedRoutine);
    }

    // 운동 루틴 삭제
    @DeleteMapping("/{routineId}")
    public ResponseEntity<String> deleteRoutine(@PathVariable Long routineId) {
        routineService.deleteRoutine(routineId);
        return ResponseEntity.ok("운동 루틴이 삭제되었습니다.");
    }
}
