package com.gbsb.routie_server.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class ExerciseLogDto {
    private Long id;              // 개별 운동 로그의 고유 ID
    private String exerciseName;  // 운동 이름
    private int duration;         // 실제 수행한 시간 (초)
    private double caloriesBurned; // 실제 소모 칼로리
}
