package com.gbsb.routie_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "step_record")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    private String userId;

    private Long exerciseId;

    // 이번 세션에서 실제로 걸은 걸음수 (end - start)
    private Integer stepsDuringSession;

    // 이 세션의 총 지속 시간(ms)
    private Long durationMillis;

    private LocalDateTime finishedAt;
}
