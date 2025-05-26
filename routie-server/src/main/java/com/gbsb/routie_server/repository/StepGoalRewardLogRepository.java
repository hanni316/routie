package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.StepGoalRewardLog;
import com.gbsb.routie_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface StepGoalRewardLogRepository extends JpaRepository<StepGoalRewardLog, Long> {
    boolean existsByUserAndGoalStepAndRewardDate(User user, int goalStep, LocalDate rewardDate);
}
