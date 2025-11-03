package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.ItemDto;
import com.gbsb.routie_server.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 전체 아이템 조회
    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItemDtos());
    }

    // 카테고리별 아이템 조회
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ItemDto>> getItemsByCategory(@PathVariable int categoryId) {
        return ResponseEntity.ok(itemService.getItemDtosByCategory(categoryId));
    }

    // 가챠 아이템 조회
    @GetMapping("/gacha")
    public ResponseEntity<List<ItemDto>> getGachaItems() {
        return ResponseEntity.ok(itemService.getGachaItemDtos());
    }
}
