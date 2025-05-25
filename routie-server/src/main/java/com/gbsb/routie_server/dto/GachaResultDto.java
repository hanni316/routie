package com.gbsb.routie_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GachaResultDto {
    private String userId; //유저 식별자
    private Long itemId; //당첨된 아이템 ID
    private boolean isSuccess;
    private boolean isHiddenItem;
}

