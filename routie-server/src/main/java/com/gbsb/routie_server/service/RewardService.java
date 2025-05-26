package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.RewardResponseDto;
import com.gbsb.routie_server.dto.StepGoalRewardResponseDto;
import com.gbsb.routie_server.entity.Reward;
import com.gbsb.routie_server.entity.StepGoalRewardLog;
import com.gbsb.routie_server.exception.ResourceNotFoundException;
import com.gbsb.routie_server.repository.RewardRepository;
import com.gbsb.routie_server.repository.StepGoalRewardLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final RewardRepository rewardRepository;
    private final StepGoalRewardLogRepository stepGoalRewardLogRepository;

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


    public StepGoalRewardResponseDto updateStepRewards(String userId, int stepCount) {
        Reward reward = rewardRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID:" + userId));

        List<StepRewardRule> rewardRules = List.of(
                new StepRewardRule(2000, 50),
                new StepRewardRule(5000, 100),
                new StepRewardRule(10000, 200)
        );

        LocalDate today = LocalDate.now();
        List<Integer> rewardedSteps = new ArrayList<>();
        int totalGold = 0;

        for (StepRewardRule rule : rewardRules) {
            boolean alreadyRewarded = stepGoalRewardLogRepository
                    .existsByUserAndGoalStepAndRewardDate(reward.getUser(), rule.goal, today);

            if (stepCount >= rule.goal && !alreadyRewarded) {
                // 보상 지급
                reward.setTotalGold(reward.getTotalGold() + rule.gold);
                totalGold += rule.gold;
                rewardedSteps.add(rule.goal);

                // 보상 기록
                stepGoalRewardLogRepository.save(
                        StepGoalRewardLog.builder()
                                .user(reward.getUser())
                                .goalStep(rule.goal)
                                .rewardDate(today)
                                .build()
                );
            }
        }

        rewardRepository.save(reward);

        return new StepGoalRewardResponseDto(rewardedSteps, totalGold,
                rewardedSteps.isEmpty() ? "이미 모든 걸음수 보상 지급됨 또는 조건 미달" : "걸음수 보상 지급 완료!");
    }

    private static class StepRewardRule {
        int goal;
        int gold;

        StepRewardRule(int goal, int gold) {
            this.goal = goal;
            this.gold = gold;
        }
    }
}
