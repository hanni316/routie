package com.gbsb.routie_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseRequestDto {
    private Long userId;
    private Long itemId;
    private int quantity;
}
