package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.AchievementDto;
import com.gbsb.routie_server.entity.Achievement;
import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.entity.UserAchievement;
import com.gbsb.routie_server.repository.AchievementRepository;
import com.gbsb.routie_server.repository.UserAchievementRepository;
import com.gbsb.routie_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;

    // 달성된 업적 조회
    public List<AchievementDto> getAchievementsByUserId(String userId) {
        return userAchievementRepository.findByUser_UserId(userId)
                .stream()
                .map(ua -> AchievementDto.fromEntity(ua.getAchievement(), true))
                .collect(Collectors.toList());
    }

    public void checkAndGrantAchievements(User user, int stepCount, int itemBuyCount, int gachaFailCount, boolean gotHiddenItem) {
        grantIfNotExists(user, "두 발 요정", stepCount >= 30000);
        grantIfNotExists(user, "산책 괴물", stepCount >= 100000);
        grantIfNotExists(user, "GPS 파괴자", stepCount >= 1000000);
        grantIfNotExists(user, "지갑오픈", itemBuyCount >= 1);
        grantIfNotExists(user, "결제 천사", itemBuyCount >= 10);
        grantIfNotExists(user, "현타 마법사", itemBuyCount >= 20);
        grantIfNotExists(user, "운빨 x망겜", gachaFailCount >= 5);
        grantIfNotExists(user, "지렸다...(축축)", gotHiddenItem);
    }

    private void grantIfNotExists(User user, String title, boolean conditionMet) {
        if (!conditionMet) return;

        List<UserAchievement> existing = userAchievementRepository.findByUser_UserId(user.getUserId());
        boolean alreadyHas = existing.stream()
                .anyMatch(ua -> ua.getAchievement().getTitle().equals(title));

        if (!alreadyHas) {
            Achievement achievement = achievementRepository.findByTitle(title)
                    .orElseThrow(() -> new RuntimeException("업적 정의 누락: " + title));

            UserAchievement userAchievement = UserAchievement.builder()
                    .user(user)
                    .achievement(achievement)
                    .build();

            userAchievementRepository.save(userAchievement);
        }
    }

    public void createAchievement(AchievementDto dto) {
        Achievement achievement = Achievement.builder()
                .code(dto.getCode())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .threshold(dto.getThreshold())
                .build();

        achievementRepository.save(achievement);
    }
}