package com.gbsb.routie_server.dto;

import com.gbsb.routie_server.entity.CharacterStyle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterStyleResponseDto {
    private Long id;
    private String userId;
    private String hair;
    private String outfit;
    private String Bottom;
    private String accessory;
    private String shoes;
    private String background;

    public CharacterStyleResponseDto(CharacterStyle entity) {
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.hair = entity.getHair();
        this.outfit = entity.getOutfit();
        this.Bottom = entity.getBottom();
        this.accessory = entity.getAccessory();
        this.shoes = entity.getShoes();
        this.background = entity.getBackground();
    }
}
