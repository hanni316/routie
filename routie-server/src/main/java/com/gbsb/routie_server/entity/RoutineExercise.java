package com.gbsb.routie_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "routine_exercise")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutineExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 고유 ID

    // 어떤 루틴에 속하는 운동인지
    @ManyToOne
    @JoinColumn(name = "routine_id", nullable = false)
    @JsonIgnore
    private Routine routine;

    // 포함된 운동 참조
    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;
}