package com.gbsb.routie_server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoutineUpdateRequestDto {
    private String name;
    private String description;
    private List<String> days; // 요일 리스트
}