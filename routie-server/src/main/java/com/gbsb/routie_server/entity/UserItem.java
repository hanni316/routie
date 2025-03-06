package com.gbsb.routie_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_items")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userItemId;  // 구매 내역 고유 번호 1부터 증가

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)  // 유저 ID (FK)
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)  // 아이템 ID (FK)
    private Item item;

    @Column(nullable = false)
    private int quantity;  // 구매 개수

    @Column(nullable = false)
    private LocalDateTime purchaseDate;  // 구매 날짜

    // 구매 시간 자동 저장
    @PrePersist
    protected void onCreate() {
        this.purchaseDate = LocalDateTime.now();
    }
}
