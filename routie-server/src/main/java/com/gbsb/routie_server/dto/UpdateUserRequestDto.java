package com.gbsb.routie_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequestDto {
    private String email;
    private String name;
    private Integer age;
    private Integer height;
    private Integer weight;
}
