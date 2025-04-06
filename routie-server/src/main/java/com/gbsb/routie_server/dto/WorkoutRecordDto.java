package com.gbsb.routie_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutRecordDto {
    private Long routineLogId;
    private Long exerciseId;
    private int duration;
}
