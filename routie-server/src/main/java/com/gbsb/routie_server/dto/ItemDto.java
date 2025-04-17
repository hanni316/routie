package com.gbsb.routie_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {
    private int itemId;
    private String name;
    private String nameEn;
    private int price;
    private String categoryName;
}
