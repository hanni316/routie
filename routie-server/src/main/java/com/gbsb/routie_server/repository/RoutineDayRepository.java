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
            r.name
        )
        FROM Routine r
        WHERE :dayOfWeek IN elements(r.days)
    """)
    List<RoutineDayDto> findByDayOfWeek(@Param("dayOfWeek") String dayOfWeek);
}
