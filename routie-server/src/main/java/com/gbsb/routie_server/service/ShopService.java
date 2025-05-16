package com.gbsb.routie_server.service;

import com.gbsb.routie_server.entity.Item;
import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.entity.UserItem;
import com.gbsb.routie_server.repository.ItemRepository;
import com.gbsb.routie_server.repository.UserItemRepository;
import com.gbsb.routie_server.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gbsb.routie_server.dto.PurchaseRequestDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ShopService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;

    public ShopService(UserRepository userRepository, ItemRepository itemRepository, UserItemRepository userItemRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.userItemRepository = userItemRepository;
    }

    @Transactional
    public String purchaseItem(PurchaseRequestDto req) {
        String userId   = req.getUserId();
        Long   itemId   = req.getItemId();
        int    quantity = req.getQuantity();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자가 없습니다: " + userId));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("아이템이 없습니다: " + itemId));

        int totalPrice = item.getPrice() * quantity;
        if (user.getGold() < totalPrice) {
            return "골드가 부족합니다.";
        }

        // 골드 차감 후 저장
        user.setGold(user.getGold() - totalPrice);
        userRepository.save(user);

        // 새로운 구매 내역 추가
        UserItem newUserItem = UserItem.builder()
                .user(user)
                .item(item)
                .quantity(quantity)
                .build();

        userItemRepository.save(newUserItem);

        return "구매했습니다!";
    }
}