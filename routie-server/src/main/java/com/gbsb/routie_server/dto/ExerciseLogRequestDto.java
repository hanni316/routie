package com.gbsb.routie_server.dto;

import lombok.Data;

@Data
public class ExerciseLogRequestDto {
    private Long exerciseId; // 수행한 운동의 ID
    private int duration;    // 실제 수행 시간 (초)
}