package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.Calorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalorieRepository extends JpaRepository<Calorie, Long> {
    List<Calorie> findByUserId(String userId);
}
