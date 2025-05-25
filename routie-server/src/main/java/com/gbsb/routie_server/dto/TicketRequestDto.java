package com.gbsb.routie_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequestDto {
    private String userId; //티켓 사용 유저 ID
    private int amount;   //사용할 티켓 수량(보통 1)
}
