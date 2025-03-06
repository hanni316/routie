package com.gbsb.routie_server.dto;

import com.gbsb.routie_server.entity.Reward;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RewardResponseDto {
    private int userId;
    private int totalGold;
    private int totalSteps;

    public RewardResponseDto(Reward reward) {
        this.userId = reward.getUser().getId();
        this.totalGold = reward.getTotalGold();
        this.totalSteps = reward.getTotalSteps();
    }
}
