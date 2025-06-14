package com.gbsb.routie_server.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExerciseResponseDto {
    private Long routineExerciseId;
    private String exerciseName;
    private String enName;
}