package com.gbsb.routie_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseRequestDto {
    private String userId; // Long → String 변경
    private Long itemId;
    private int quantity;
}
