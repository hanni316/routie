package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.RoutineRequestDto;
import com.gbsb.routie_server.dto.RoutineResponseDto;
import com.gbsb.routie_server.dto.RoutineUpdateRequestDto;
import com.gbsb.routie_server.entity.Routine;
import com.gbsb.routie_server.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/routines")
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;

    // 운동 루틴 생성
    @PostMapping("/{userId}")
    public ResponseEntity<RoutineResponseDto> createRoutineWithExercises(
            @PathVariable String userId,
            @RequestBody RoutineRequestDto dto) {

        Routine created = routineService.createRoutineWithExercises(userId, dto);
        return ResponseEntity.ok(new RoutineResponseDto(created));
    }

    // 특정 사용자의 루틴 목록 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<RoutineResponseDto>> getUserRoutines(@PathVariable String userId) {
        List<RoutineResponseDto> list = routineService.getUserRoutines(userId).stream()
                .map(RoutineResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // 단일 루틴의 전체 정보
    @GetMapping("/detail/{routineId}")
    public ResponseEntity<RoutineResponseDto> getRoutineDetail(@PathVariable Long routineId) {
        Routine routine = routineService.getRoutine(routineId);
        if (routine == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new RoutineResponseDto(routine));
    }
    // 루틴 수정(루틴 이름, 루틴 설명만)
    @PutMapping("/{routineId}")
    public ResponseEntity<RoutineResponseDto> updateRoutine(
            @PathVariable Long routineId,
            @RequestBody RoutineUpdateRequestDto dto) {

        Routine updated = routineService.updateRoutine(routineId, dto);
        return ResponseEntity.ok(new RoutineResponseDto(updated));
    }

    // 루틴 삭제(안에 운동도 함께 다 삭제)
    @DeleteMapping("/{routineId}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long routineId) {
        routineService.deleteRoutine(routineId);
        return ResponseEntity.noContent().build();
    }
}