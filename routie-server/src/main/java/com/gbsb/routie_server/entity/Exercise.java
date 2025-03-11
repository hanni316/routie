package com.gbsb.routie_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercise")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 운동 고유 ID

    @Column(nullable = false, unique = true)
    private String name;  // 운동 이름

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private ExerciseCategory category;  // 운동 카테고리

    @Column(nullable = false)
    private double caloriesPerSecond;  // 초당 소모 칼로리 (kcal/sec)
}
