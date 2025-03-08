package com.gbsb.routie_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table (name = "calories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Calorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false) // userId FK 설정
    private User user;

    @Column(nullable = false)
    private double caloriesBurned; // 일주일 동안 누적된 칼로리

    @Column(nullable = false)
    private LocalDate weekStartDate; // 해당 주의 시작 날짜

    @Column(nullable = false)
    private LocalDateTime lastUpdated; // 마지막 업데이트 시간

    // 새로운 주가 시작될 때 0으로 초기화 하기 위한 생성자
    public Calorie(User user, LocalDate weekStartDate) {
        this.user = user;
        this.caloriesBurned = 0;
        this.weekStartDate = weekStartDate;
        this.lastUpdated = LocalDateTime.now();
    }

    // 칼로리 추가 메서드
    public void addCalories(double calories) {
        this.caloriesBurned += calories;
        this.lastUpdated = LocalDateTime.now();
    }
}
