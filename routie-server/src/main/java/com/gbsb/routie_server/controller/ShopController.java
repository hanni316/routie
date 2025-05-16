package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.PurchaseRequestDto;
import com.gbsb.routie_server.service.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop")
public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    // 아이템 구매
    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseItem(@RequestBody PurchaseRequestDto request) {
        String result = shopService.purchaseItem(request);
        return ResponseEntity.ok(result);
    }
}