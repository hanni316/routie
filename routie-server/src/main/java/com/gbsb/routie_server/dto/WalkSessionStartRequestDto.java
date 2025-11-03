package com.gbsb.routie_server.dto;

import lombok.Data;

@Data
public class WalkSessionStartRequestDto {
    private String userId;
    private Long exerciseId;
    private Integer startStepCount;
}
