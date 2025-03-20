package com.gbsb.routie_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gbsb.routie_server.entity.RoutineLog;

public interface RoutineLogRepository extends JpaRepository<RoutineLog, Long> {

}