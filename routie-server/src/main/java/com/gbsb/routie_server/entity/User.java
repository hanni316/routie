package com.gbsb.routie_server.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "user")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(nullable = false)
    private String userId; // 로그인 시 사용하는 ID id -> userId

    @Column(nullable = false, unique = true)
    private String email; // 이메일 (중복 불가)

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false, unique = true)
    private String name; // 사용자 이름 (중복 불가)

    @Builder.Default
    @Column(nullable = false)
    private int gold = 0; // 골드 초기값 0

    @Builder.Default
    @Column(nullable = false)
    private boolean isAdmin = false; // 유저 : false, 관리자 : true

    // 유저가 기록한 칼로리 데이터 (1:N 관계)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Calorie> calories;

    // 누적된 총 칼로리 계산 메서드
    public double getTotalCaloriesBurned() {
        if (calories == null) {
            return 0;
        }
        return calories.stream().mapToDouble(Calorie::getCaloriesBurned).sum();
    }
}
