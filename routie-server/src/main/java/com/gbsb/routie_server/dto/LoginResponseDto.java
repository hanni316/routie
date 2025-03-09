package com.gbsb.routie_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String userId;  //
    private String name;    //
    private int gold;       //
}