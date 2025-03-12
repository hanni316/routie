package com.gbsb.routie_server.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class RoutineRequestDto {
    private String name;  // 루틴 이름
    private String description;  // 루틴 설명 (선택 사항)
}
