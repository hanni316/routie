package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.RewardResponseDto;
import com.gbsb.routie_server.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reward")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    @GetMapping("/{userId}")
    public ResponseEntity<RewardResponseDto> getUserReward(@PathVariable Long userId) {
        RewardResponseDto reward = rewardService.getRewardBuUserId(userId);
        return ResponseEntity.ok(reward);
    }
}
