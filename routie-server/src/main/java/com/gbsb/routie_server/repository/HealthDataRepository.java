package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.HealthData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthDataRepository extends JpaRepository<HealthData, Long> {
// 필요 시 사용자 ID로 조회하는 메소드도 추가 가능합니둥~
}