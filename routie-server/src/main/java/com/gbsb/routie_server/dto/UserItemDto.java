// UserItemDto.java
package com.gbsb.routie_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class UserItemDto {
    private int itemId;
    private String name;
    private String nameEn;
    private int price;
    private String categoryName;
    private boolean gachaItem;
}
