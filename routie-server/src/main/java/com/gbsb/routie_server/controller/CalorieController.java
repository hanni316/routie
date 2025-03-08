package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.entity.Calorie;
import com.gbsb.routie_server.repository.CalorieRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/calories")
public class CalorieController {
    private final CalorieRepository calorieRepository;

    public CalorieController(CalorieRepository calorieRepository) {
        this.calorieRepository = calorieRepository;
    }

    @PostMapping
    public ResponseEntity<String> saveCalorie(@RequestBody Calorie calorie) {
        calorie.setLastUpdated(LocalDateTime.now());// 현재 시간 저장
        calorieRepository.save(calorie); // 데이터 저장
        return ResponseEntity.ok("칼로리 데이터 저장 완료");
    }
}
