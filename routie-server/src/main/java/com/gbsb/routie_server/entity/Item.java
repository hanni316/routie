
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
    private String name;  // 아이템 이름

    @Column(nullable = false)
    private int price;  // 아이템 가격

    @Column(nullable = false)
    private int categoryId;  // 카테고리 ID (외래키)
}
