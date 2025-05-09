package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    List<UserAchievement> findByUser_UserId(String userId);
}

