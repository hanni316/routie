package com.gbsb.routie_server.dto;

import lombok.Data;
@Data
public class WalkSessionEndRequestDto {
    private Long sessionId;
    private Integer endStepCount;
    private Long durationMillis;
}
