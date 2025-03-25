package com.gbsb.routie_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gbsb.routie_server.entity.RoutineLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoutineLogRepository extends JpaRepository<RoutineLog, Long> {
    @Query(value = "SELECT * FROM routine_log WHERE user_id = :userId AND DATE(completion_date) = :date", nativeQuery = true)
    List<RoutineLog> findByUserIdAndDate(@Param("userId") String userId, @Param("date") LocalDate date);
}