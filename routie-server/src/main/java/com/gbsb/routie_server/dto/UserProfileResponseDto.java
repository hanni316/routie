package com.gbsb.routie_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDto {
    private String userId;
    private String name;
    private int gold;
}