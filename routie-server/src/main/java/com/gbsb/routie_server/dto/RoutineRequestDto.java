package com.gbsb.routie_server.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class RoutineRequestDto {
    private String name;  // 루틴 이름
    private String description;  // 루틴 설명 (선택 사항)
    private List<String> days;
    private List<ExerciseRequestDto> exercises;
    private LocalDate scheduledDate;
}