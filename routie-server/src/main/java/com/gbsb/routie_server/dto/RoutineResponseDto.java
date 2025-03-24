package com.gbsb.routie_server.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gbsb.routie_server.entity.Routine;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RoutineResponseDto {
    private Long id;
    private String name;
    private String description;
    private List<String> exercises; // 변경: ExerciseResponseDto 대신 String 리스트

    public RoutineResponseDto(Routine routine) {
        this.id = routine.getId();
        this.name = routine.getName();
        this.description = routine.getDescription();
        this.exercises = routine.getExercises().stream()
                .map(re -> re.getExercise().getName())  // 운동 이름만 추출
                .collect(Collectors.toList());
    }
}
