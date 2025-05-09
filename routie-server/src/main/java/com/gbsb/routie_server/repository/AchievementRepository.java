package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Optional<Achievement> findByTitle(String title);
}

