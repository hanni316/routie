package com.gbsb.routie_server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "routine")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)  // 사용자와 연결
    private User user;

    @Column(nullable = false)
    private String name;  // 운동 루틴 이름

    @Column(nullable = false)
    private String description;  // 운동 루틴 설명

    @Column(nullable = false)
    private int duration;  // 운동 시간 (분)

    @Column(nullable = false)
    private int caloriesBurned;  // 예상 소모 칼로리
}
