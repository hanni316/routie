package com.gbsb.routie_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "step_session")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    // 누가 걷고 있는지
    private String userId;

    // 어떤 운동인지 (걷기 운동 exerciseId)
    private Long exerciseId;

    // 세션 시작 시 워치에서 읽은 누적 걸음수 (baseline)
    private Integer startStepCount;

    private LocalDateTime startedAt;
}
