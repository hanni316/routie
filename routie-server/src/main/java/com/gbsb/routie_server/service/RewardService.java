package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.RewardResponseDto;
import com.gbsb.routie_server.entity.Reward;
import com.gbsb.routie_server.exception.ResourceNotFoundException;
import com.gbsb.routie_server.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final RewardRepository rewardRepository;

    public RewardResponseDto getRewardBuUserId(String userId) {
        Reward reward = rewardRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID:" + userId));

        return new RewardResponseDto(reward);
    }

    public RewardResponseDto updateCalories(String userId, int caloriesBurned) {
        Reward reward = rewardRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID:" + userId));

        // 칼로리를 Gold로 변환 (예: 1kcal = 10 Gold)
        int earnedGold = caloriesBurned * 10;
        reward.setTotalGold(reward.getTotalGold() + earnedGold);
        rewardRepository.save(reward);

        return new RewardResponseDto(reward);
    }


}
