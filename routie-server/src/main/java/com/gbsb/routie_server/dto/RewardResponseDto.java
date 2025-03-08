package com.gbsb.routie_server.dto;

import com.gbsb.routie_server.entity.Reward;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RewardResponseDto {
    private String userId;
    private int totalGold;
    private int totalSteps;

    public RewardResponseDto(Reward reward) {
        this.userId = reward.getUser().getUserId(); // getId() → getUserid()
        this.totalGold = reward.getTotalGold();
        this.totalSteps = reward.getTotalSteps();
    }
}
