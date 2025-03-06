package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.entity.Item;
import com.gbsb.routie_server.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    //  아이템 조회
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    // 카테고리별 아이템 조회
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Item>> getItemsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(itemService.getItemsByCategory(categoryId));
    }
}
