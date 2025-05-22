package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.TicketLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketLogRepository extends JpaRepository<TicketLog, Long> {
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TicketLog t WHERE t.user.userId = :userId")
    int getTicketCountByUserId(@Param("userId") String userId);
}

