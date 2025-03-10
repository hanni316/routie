package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.RoutineExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoutineExerciseRepository extends JpaRepository<RoutineExercise, Long> {
    // 특정 루틴에 포함된 운동 목록 조회
    List<RoutineExercise> findByRoutine_Id(Long routineId);
}