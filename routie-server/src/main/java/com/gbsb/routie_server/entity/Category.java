package com.gbsb.routie_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;  // 카테고리 ID

    @Column(nullable = false, unique = true)
    private String name;  // 카테고리 이름

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Item> items; // 아이템 리스트 (1:N 관계)
}
