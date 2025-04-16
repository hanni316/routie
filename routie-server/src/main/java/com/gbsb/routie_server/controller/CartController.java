package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.CartItemDto;
import com.gbsb.routie_server.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니에 아이템 추가
    @PostMapping("/{userId}")
    public ResponseEntity<Void> addToCart(@PathVariable String userId, @RequestBody CartItemDto dto) {
        cartService.addToCart(userId, dto);
        return ResponseEntity.ok().build();
    }

    // 장바구니 목록 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemDto>> getCartItems(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCartItems(userId));
    }

    // 장바구니 아이템 삭제
    @DeleteMapping("/{userId}/{itemId}")
    public ResponseEntity<Void> deleteFromCart(@PathVariable String userId, @PathVariable int itemId) {
        cartService.removeFromCart(userId, itemId);
        return ResponseEntity.noContent().build();
    }

}
