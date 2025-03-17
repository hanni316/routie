package com.gbsb.routie_server.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

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

    @Column(nullable = true)
    private String description;  // 루틴 설명 (선택 사항)

    @Column(nullable = false)
    private double caloriesBurned;  // 소모 칼로리

    @Column(nullable = false)
    private int duration;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoutineExercise> exercises;  // 루틴에 포함된 운동들

    public double getTotalCaloriesBurned() {
        if (exercises == null || exercises.isEmpty()) {
            return 0;
        }
        return exercises.stream().mapToDouble(RoutineExercise::getCaloriesBurned).sum();

    }
    /*public void updateRoutineStats() { 얘 실행 안 됨.
        if (exercises == null || exercises.isEmpty()) {
            this.duration = 0;
            this.caloriesBurned = 0;
            return;
        }

        this.duration = exercises.stream().mapToInt(RoutineExercise::getDuration).sum();
        this.caloriesBurned = exercises.stream().mapToDouble(RoutineExercise::getCaloriesBurned).sum();
    }*/
}
