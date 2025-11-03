
package com.gbsb.routie_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "items")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemId;  // 아이템 ID

    @Column(nullable = false, unique = true)
    private String nameEn;  // 아이템 영어 이름

    @Column(nullable = false, unique = true)
    private String name;  // 아이템 이름

    @Column(nullable = false)
    private int price;  // 아이템 가격

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)  // FK로 저장
    private Category category;

    @Column(nullable = false)
    private boolean gachaItem = false;
}
