package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.CaloriesRequestDto;
import com.gbsb.routie_server.dto.RewardResponseDto;
import com.gbsb.routie_server.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reward")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    @GetMapping("/{userId}")
    public ResponseEntity<RewardResponseDto> getUserReward(@PathVariable String userId) {
        RewardResponseDto reward = rewardService.getRewardBuUserId(userId);
        return ResponseEntity.ok(reward);
    }

    @PostMapping("/{userId}/calories")
    public ResponseEntity<RewardResponseDto> updateCalories(
            @PathVariable String userId,
            @RequestBody CaloriesRequestDto request) {

        RewardResponseDto updatedReward = rewardService.updateCalories(userId, request.getCaloriesBurned());
        return ResponseEntity.ok(updatedReward);
    }

}
