package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.ExerciseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ExerciseCategoryRepository extends JpaRepository<ExerciseCategory, Long> {
    Optional<ExerciseCategory> findByName(String name); // 카테고리 이름으로 조회
}
