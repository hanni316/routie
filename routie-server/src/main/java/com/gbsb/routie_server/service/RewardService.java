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

    public RewardResponseDto getRewardBuUserId(Long userId) {
        Reward reward = rewardRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID:" + userId));

        return new RewardResponseDto(reward);
    }

}
