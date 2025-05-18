package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.dto.RoutineDayDto;
import com.gbsb.routie_server.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoutineDayRepository extends JpaRepository<Routine, Long> {

    @Query("""
        SELECT new com.gbsb.routie_server.dto.RoutineDayDto(
            r.id,
            r.name,
            r.scheduledDate
        )
        FROM Routine r
        WHERE r.user.userId   = :userId
          AND :dayOfWeek IN elements(r.days)
    """)
    List<RoutineDayDto> findByUserIdAndDayOfWeek(
            @Param("userId")   String userId,
            @Param("dayOfWeek") String dayOfWeek
    );
}