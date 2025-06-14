package com.gbsb.routie_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDto {
    private Long exerciseId;
    private Long routineExerciseId;
    private String exerciseName;
}
