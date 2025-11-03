package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.StepRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StepRecordRepository extends JpaRepository<StepRecord, Long> {
}
