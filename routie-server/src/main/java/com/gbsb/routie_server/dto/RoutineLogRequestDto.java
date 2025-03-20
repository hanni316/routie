package com.gbsb.routie_server.dto;

import lombok.Data;
import java.util.List;

@Data
public class RoutineLogRequestDto {
    private Long routineId;                        // 완료한 루틴 ID
    private List<ExerciseLogRequestDto> exercises; // 실제 수행된 운동 데이터 리스트
}