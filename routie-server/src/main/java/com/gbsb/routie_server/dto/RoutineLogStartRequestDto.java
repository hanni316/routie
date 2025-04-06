package com.gbsb.routie_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoutineLogStartRequestDto {
    private Long routineId;
    private String userId;
}
