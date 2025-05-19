package com.gbsb.routie_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseRequestDto {
    private Long exerciseId;
    private int duration;
    private String enName;
}
