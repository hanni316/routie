package com.gbsb.routie_server.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "routine_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutineLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 완료한 루틴 참조 (템플릿 정보)
    @ManyToOne
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    // 해당 루틴을 완료한 사용자 정보
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 루틴 완료 시점
    @Column(nullable = false)
    private LocalDateTime completionDate;

    // 실제 수행된 총 소모 칼로리
    @Column(nullable = false)
    private double totalCaloriesBurned;

    // 실제 수행된 총 운동 시간 (초)
    @Column(nullable = false)
    private int totalDuration;

    // 각 운동별 실제 수행 로그 목록
    @OneToMany(mappedBy = "routineLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseLog> exerciseLogs;

    // 완료 시점의 루틴 구성 스냅샷 (JSON 문자열로 저장)
    @Lob
    @Column(name = "routine_snapshot", columnDefinition = "TEXT", nullable = false)
    private String routineSnapshot;
}
