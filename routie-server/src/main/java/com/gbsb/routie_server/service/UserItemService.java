// UserItemService.java
package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.GachaResultDto;
import com.gbsb.routie_server.dto.UserItemDto;
import com.gbsb.routie_server.entity.Item;
import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.entity.UserItem;
import com.gbsb.routie_server.repository.ItemRepository;
import com.gbsb.routie_server.repository.UserItemRepository;
import com.gbsb.routie_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserItemService {

    private UserItemRepository repo;
    private ItemRepository itemRepo;
    private UserRepository userRepo;

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
    //가챠 당첨 아이템 저장(중복 방지 포함)
    public void saveGachaItem(GachaResultDto dto) {
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저"));

        Item item = itemRepo.findById(dto.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이템"));

        // 이미 가지고 있으면 저장 안 함 (중복 방지)
        Optional<UserItem> existing = repo.findByUserAndItem(user, item);
        if (existing.isPresent()) return;

        UserItem userItem = UserItem.builder()
                .user(user)
                .item(item)
                .quantity(1)
                .build();

        repo.save(userItem);
    }

}
