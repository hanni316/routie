package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(String userId);
    void deleteByUserIdAndItem_ItemId(String userId, int itemId);
}
