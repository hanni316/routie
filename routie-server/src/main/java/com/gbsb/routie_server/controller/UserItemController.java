// UserItemController.java
package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.UserItemDto;
import com.gbsb.routie_server.service.UserItemService;
import lombok.RequiredArgsConstructor;
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
}