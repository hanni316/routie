package com.gbsb.routie_server.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import com.gbsb.routie_server.entity.RoutineLog;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Data
public class RoutineLogDto {
    @JsonIgnore
    private Long id;                           // 루틴 로그의 고유 ID
    @JsonIgnore
    private RoutineResponseDto routine;        // 해당 루틴의 기본 정보 (템플릿 정보)
    @JsonIgnore
    private LocalDateTime completionDate;      // 루틴 완료 시점
    private double totalCaloriesBurned;        // 실제 수행한 총 소모 칼로리
    private int totalDuration;                 // 실제 수행한 총 운동 시간 (초)
    private List<ExerciseLogDto> exerciseLogs; // 각 운동별 실제 수행 로그 목록
    @JsonIgnore
    private String routineSnapshot;            // 완료 시점의 루틴 구성 스냅샷 (JSON 문자열)

    public RoutineLogDto(RoutineLog log) {
        this.exerciseLogs = log.getExerciseLogs().stream()
                .map(el -> {
                    ExerciseLogDto dto = new ExerciseLogDto();
                    dto.setId(el.getId());
                    dto.setExerciseName(el.getExercise().getName());
                    dto.setDuration(el.getDuration());
                    dto.setCaloriesBurned(el.getCaloriesBurned());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
