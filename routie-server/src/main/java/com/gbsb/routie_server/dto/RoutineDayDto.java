package com.gbsb.routie_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoutineDayDto {
    private  Long id;
    private  String name;

    public RoutineDayDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
