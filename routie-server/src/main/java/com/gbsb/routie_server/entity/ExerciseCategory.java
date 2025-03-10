package com.gbsb.routie_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "exercise_category")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 카테고리 고유 ID

    @Column(nullable = false, unique = true)
    private String name;  // 카테고리 이름

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exercise> exercises;
}
