package com.gbsb.routie_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "routine_exercise")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutineExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 고유 ID

    @ManyToOne
    @JoinColumn(name = "routine_id", nullable = false)
    @JsonIgnore
    private Routine routine;  // 어떤 루틴에 속하는 운동인지

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;  // 어떤 운동인지

    @Column(nullable = false)
    private int duration;  // 사용자가 입력한 운동 시간 (초)

    @Column(nullable = false, columnDefinition = "int default 0")
    private double caloriesBurned;  // 운동 시간에 따른 소모 칼로리 값

}
