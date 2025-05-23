package com.gbsb.routie_server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TicketUseRequestDto {
    private String userId;
    private int amount;
}
