package com.gbsb.routie_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String userId;
    private String email;
    private String password;
    private String name;
    private String gender;
    private Integer age;
    private Integer height;
    private Integer weight;
}
