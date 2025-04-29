package com.gbsb.routie_server.entity;

import jakarta.persistence.*;

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

    @Column(nullable = false)
    private int age; // 나이

    @Column(nullable = false)
    private String gender; // 성별

    @Column(nullable = false)
    private int height; // 키

    @Column(nullable = false)
    private int weight; // 몸무게

    @Builder.Default
    @Column(nullable = false)
    private int gold = 0; // 골드 초기값 0

    @Builder.Default
    @Column(nullable = false)
    private boolean isAdmin = false; // 유저 : false, 관리자 : true

    @Column
    private String profileImageUrl;
}
