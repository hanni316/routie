package com.gbsb.routie_server.service;

import com.gbsb.routie_server.entity.Item;
import com.gbsb.routie_server.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // 카테고리 아이템 조회
    public List<Item> getItemsByCategory(Long categoryId) {
        return itemRepository.findByCategoryId(categoryId);
    }

    // 전체 아이템 조회
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
}
