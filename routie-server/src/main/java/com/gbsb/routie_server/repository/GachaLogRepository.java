package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.GachaLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface GachaLogRepository extends JpaRepository<GachaLog, Long> {
}
