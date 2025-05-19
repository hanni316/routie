package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.dto.UserRankingProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gbsb.routie_server.entity.RoutineLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RoutineLogRepository extends JpaRepository<RoutineLog, Long> {
    @Query(value = "SELECT * FROM routine_log WHERE user_id = :userId AND DATE(completion_date) = :date", nativeQuery = true)
    List<RoutineLog> findByUserIdAndDate(@Param("userId") String userId, @Param("date") LocalDate date);

    @Query("""
    SELECT u.userId as userId, u.name as nickname, u.profileImageUrl as profileImageUrl,
           SUM(r.totalCaloriesBurned) as total
    FROM RoutineLog r
    JOIN r.user u
    WHERE r.completionDate BETWEEN :start AND :end
    GROUP BY u.userId
    ORDER BY total DESC
""")
    List<UserRankingProjection> getWeeklyCalorieRanking(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}