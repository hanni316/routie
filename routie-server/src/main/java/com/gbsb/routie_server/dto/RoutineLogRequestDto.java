package com.gbsb.routie_server.dto;

import lombok.Data;
import java.util.List;

@Data
public class RoutineLogRequestDto {
    private Long routineId;
    private String userId;
    private List<ExerciseLogRequestDto> exercises; // 실제 수행된 운동 데이터 리스트
}