package com.gbsb.routie_server.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankingResultDto {
    private int myRank;
    private List<RankingResponseDto> ranking;
}
