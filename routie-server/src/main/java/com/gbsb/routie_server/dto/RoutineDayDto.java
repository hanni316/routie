package com.gbsb.routie_server.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RoutineDayDto {
    private  Long id;
    private  String name;

    private LocalDate scheduledDate;

    public RoutineDayDto(Long id, String name, LocalDate scheduledDate) {
        this.id = id;
        this.name = name;
        this.scheduledDate = scheduledDate;
    }
}
