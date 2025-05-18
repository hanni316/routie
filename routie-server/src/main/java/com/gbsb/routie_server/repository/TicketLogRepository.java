package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.TicketLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketLogRepository extends JpaRepository<TicketLog, Long> {
}
