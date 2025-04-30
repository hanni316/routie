package com.gbsb.routie_server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercise_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 고유 ID

    // 이 운동 로그가 속한 루틴 실행 로그
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_log_id", nullable = false)
    private RoutineLog routineLog;

    // 실제 수행된 운동 정보 (기존 Exercise 엔티티와 연관)
    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    // 실제 운동 수행 시간(초)
    @Builder.Default
    @Column(nullable = false)
    private int duration = 0;

    // 실제 소모된 칼로리 값
    @Builder.Default
    @Column(nullable = false)
    private double caloriesBurned = 0.0;

}