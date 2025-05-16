// UserItemService.java
package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.UserItemDto;
import com.gbsb.routie_server.entity.UserItem;
import com.gbsb.routie_server.repository.UserItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserItemService {
    private final UserItemRepository repo;
    public UserItemService(UserItemRepository repo) { this.repo = repo; }

    public List<UserItemDto> getPurchasedItems(String userId) {
        List<UserItem> items = repo.findByUser_UserId(userId);
        return items.stream()
                .map(ui -> new UserItemDto(
                        ui.getItem().getItemId(),
                        ui.getItem().getName(),
                        ui.getItem().getNameEn(),
                        ui.getItem().getPrice(),
                        ui.getItem().getCategory().getName()
                ))
                .collect(Collectors.toList());
    }
}
