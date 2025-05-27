package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.AchievementDto;
import com.gbsb.routie_server.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    // 사용자의 업적 조회
    @GetMapping("/user/{userId}")
    public List<AchievementDto> getAllAchievements(@PathVariable String userId) {
        return achievementService.getAllAchievementsWithStatus(userId);
    }

    // 업적 생성
    @PostMapping
    public void createAchievement(@RequestBody AchievementDto dto) {
        achievementService.createAchievement(dto);
    }
}
