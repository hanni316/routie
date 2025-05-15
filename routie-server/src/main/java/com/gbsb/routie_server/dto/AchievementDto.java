package com.gbsb.routie_server.dto;

import com.gbsb.routie_server.entity.Achievement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Builder
@AllArgsConstructor
public class AchievementDto {
    private Long id;
    private String title;
    private String description;
    private String code;
    private String category;
    private int threshold;
    private boolean achieved;

    public static AchievementDto fromEntity(Achievement achievement, boolean achieved) {
        return AchievementDto.builder()
                .id(achievement.getId())
                .title(achievement.getTitle())
                .description(achievement.getDescription())
                .code(achievement.getCode())
                .category(achievement.getCategory())
                .threshold(achievement.getThreshold())
                .achieved(achieved)
                .build();
    }
}
