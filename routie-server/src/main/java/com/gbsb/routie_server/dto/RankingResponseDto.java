package com.gbsb.routie_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class RankingResponseDto {
    private int rank;
    private String userId;
    private String nickname;
    private String profileImageUrl;
    private double value;
}
