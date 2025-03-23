package com.gbsb.routie_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PasswordUpdateRequestDto {
    private String currentPassword;
    private String newPassword;
}
