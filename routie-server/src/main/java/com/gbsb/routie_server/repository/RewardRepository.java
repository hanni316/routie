package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RewardRepository extends JpaRepository<Reward, Long> {
    Optional<Reward> findByUser_UserId(String userId);
    void deleteByUser_UserId(String userId);
}
