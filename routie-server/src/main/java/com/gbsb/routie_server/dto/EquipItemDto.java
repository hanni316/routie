package com.gbsb.routie_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipItemDto {
    private String categoryName; // 예: 헤어, 상의, 하의
    private String itemName; // 예: 긴머리, 티셔츠, 반바지
}
