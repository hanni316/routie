package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    // 특정 카테고리 아이템 가져오기
    List<Item> findByCategory_CategoryId(int categoryId);
    // 가챠 아이템 O
    List<Item> findByGachaItemTrue();

}
