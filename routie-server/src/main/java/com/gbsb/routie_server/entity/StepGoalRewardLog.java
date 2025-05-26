package com.gbsb.routie_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

//걸음수 중복 보상 방지를 위해 엔티티 생성합니다.

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StepGoalRewardLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private int goalStep; // 2000, 5000, 10000

    private LocalDate rewardDate;
}
