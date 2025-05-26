package com.gbsb.routie_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StepGoalRewardResponseDto {
    private List<Integer> rewardedSteps;
    private int goldAdded;
    private String message;
}
