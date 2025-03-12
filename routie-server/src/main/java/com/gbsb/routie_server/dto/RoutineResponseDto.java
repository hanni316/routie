package com.gbsb.routie_server.dto;

import com.gbsb.routie_server.entity.Routine;
import lombok.Getter;

@Getter
public class RoutineResponseDto {
    private Long id;                 // 루틴 ID
    private String name;             // 루틴 이름
    private String description;      // 루틴 설명
    private double totalCaloriesBurned;  // 루틴의 총 소모 칼로리

    public RoutineResponseDto(Routine routine) {
        this.id = routine.getId();
        this.name = routine.getName();
        this.description = routine.getDescription();
        this.totalCaloriesBurned = routine.getTotalCaloriesBurned();
    }
}
