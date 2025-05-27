package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.AchievementDto;
import com.gbsb.routie_server.entity.Achievement;
import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.entity.UserAchievement;
import com.gbsb.routie_server.repository.AchievementRepository;
import com.gbsb.routie_server.repository.UserAchievementRepository;
import com.gbsb.routie_server.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final UserRepository userRepository;

    // 달성된 업적 조회
    public List<AchievementDto> getAllAchievementsWithStatus(String userId) {
        List<Achievement> allAchievements = achievementRepository.findAll();

        // 달성한 업적 ID만 모으기
        List<Long> achievedIds = userAchievementRepository.findByUser_UserId(userId)
                .stream()
                .map(ua -> ua.getAchievement().getId())
                .toList();

        // 전체 업적 목록 + 달성 여부 처리
        return allAchievements.stream()
                .map(a -> AchievementDto.fromEntity(a, achievedIds.contains(a.getId())))
                .collect(Collectors.toList());
    }

    // 걸음 수 업적
    public void checkStepAchievements(User user, int stepCount) {
        grantIfNotExists(user, "두 발 요정", stepCount >= 30000);
        grantIfNotExists(user, "산책 괴물", stepCount >= 100000);
        grantIfNotExists(user, "GPS 파괴자", stepCount >= 1000000);
    }

    // 아이템 구매 업적
    public void checkPurchaseAchievements(User user, int itemBuyCount) {

        grantIfNotExists(user, "지갑오픈", itemBuyCount >= 1);
        grantIfNotExists(user, "결제 천사", itemBuyCount >= 10);
        grantIfNotExists(user, "현타 마법사", itemBuyCount >= 20);
    }

    // 가챠 실패 없적
    public void checkGachaAchievements(User user, int gachaFailCount) {
        grantIfNotExists(user, "운빨 X망겜", gachaFailCount >= 5);
    }

    // 가챠 획득 업적
    public void checkHiddenAchievements(User user, boolean gotHiddenItem) {
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

            // 업적 보상 지급 (추가)
            int rewardGold = achievement.getRewardGold(); // 또는 하드코딩된 값
            user.setGold(user.getGold() + rewardGold);
            userRepository.save(user);

            // 로그 출력 or 리워드 내역 기록할 수도 있음
            System.out.println("[" + title + "] 업적을 달성하고 " + rewardGold + "골드를 받았습니다!");
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