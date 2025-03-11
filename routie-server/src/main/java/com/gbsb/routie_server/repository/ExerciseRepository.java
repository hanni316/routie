package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    // 특정 카테고리에 속한 운동 목록 조회
    List<Exercise> findByCategory_Id(Long categoryId);
}