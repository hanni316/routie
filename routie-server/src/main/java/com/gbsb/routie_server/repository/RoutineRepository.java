package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findByUser_UserId(String userId); // 특정 사용자의 루틴 목록 조회
    void deleteByUser_UserId(String userId);
    List<Routine> findByUserUserIdAndScheduledDate(String userId, LocalDate date);
}
