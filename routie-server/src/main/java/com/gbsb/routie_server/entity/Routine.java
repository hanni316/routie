package com.gbsb.routie_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "routine")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 해당 루틴을 생성한 사용자
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;  // 루틴 이름

    @Column
    private String description;  // 루틴 설명

    /*@Builder.Default
    @Column(name = "scheduled_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date scheduledDate = new Date();  // 루틴 날짜 (기본값은 오늘 날짜)*/

    // 루틴에 포함된 운동들
    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<RoutineExercise> exercises = new ArrayList<>();
}