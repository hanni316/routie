package com.gbsb.routie_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WalkSessionEndResponseDto {
    private Integer stepsDuringSession;
    private Long durationMillis;
}
