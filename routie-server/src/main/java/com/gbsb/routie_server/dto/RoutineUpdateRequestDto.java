package com.gbsb.routie_server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoutineUpdateRequestDto {
    private String name;
    private String description;
}