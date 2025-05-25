package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.GachaLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GachaLogRepository extends JpaRepository<GachaLog, Long> {
    //실패횟수 조회용
    @Query("SELECT COUNT(g) FROM GachaLog g WHERE g.user.userId = :userId AND g.item IS NULL")
    int countFailuresByUserId(@Param("userId") String userId);
}
