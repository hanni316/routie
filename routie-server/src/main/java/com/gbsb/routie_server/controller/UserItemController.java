// UserItemController.java
package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.GachaResultDto;
import com.gbsb.routie_server.dto.UserItemDto;
import com.gbsb.routie_server.service.UserItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/items")
@RequiredArgsConstructor
public class UserItemController {
    private final UserItemService userItemService;

    @GetMapping("/{userId}")
    public List<UserItemDto> getUserItems(@PathVariable String userId) {
        return userItemService.getPurchasedItems(userId);
    }

    @PostMapping("/gacha")
    public ResponseEntity<String> saveGachaItem(@RequestBody GachaResultDto dto) {
        userItemService.saveGachaItem(dto);
        return ResponseEntity.ok("당첨 아이템 저장 완료");
    }

    //아이템 구매 개수
    @GetMapping("/{userId}/total")
    public ResponseEntity<Integer> getTotalQuantity(@PathVariable String userId) {
        int total = userItemService.getTotalQuantity(userId);
        return ResponseEntity.ok(total);
    }
}