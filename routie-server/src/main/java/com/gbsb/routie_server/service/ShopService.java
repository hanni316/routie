package com.gbsb.routie_server.service;

import com.gbsb.routie_server.entity.Item;
import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.entity.UserItem;
import com.gbsb.routie_server.repository.ItemRepository;
import com.gbsb.routie_server.repository.UserItemRepository;
import com.gbsb.routie_server.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public String purchaseItem(String userId, Long itemId, int quantity) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Item> itemOpt = itemRepository.findById(itemId);

        if (userOpt.isEmpty() || itemOpt.isEmpty()) {
            return "사용자 또는 아이템이 존재하지 않습니다.";
        }

        User user = userOpt.get();
        Item item = itemOpt.get();
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
                .purchaseDate(LocalDateTime.now())
                .build();

        userItemRepository.save(newUserItem);

        return "✅ " + item.getName() + "을(를) " + quantity + "개 구매했습니다!";
    }
}