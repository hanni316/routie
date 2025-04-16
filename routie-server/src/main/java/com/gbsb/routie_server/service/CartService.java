package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.CartItemDto;
import com.gbsb.routie_server.entity.CartItem;
import com.gbsb.routie_server.entity.Item;
import com.gbsb.routie_server.repository.CartItemRepository;
import com.gbsb.routie_server.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;

    public void addToCart(String userId, CartItemDto dto) {
        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("아이템이 존재하지 않습니다."));

        CartItem cartItem = CartItem.builder()
                .userId(userId)
                .item(item)
                .build();

        cartItemRepository.save(cartItem);
    }

    public List<CartItemDto> getCartItems(String userId) {
        return cartItemRepository.findByUserId(userId).stream()
                .map(cartItem -> {
                    CartItemDto dto = new CartItemDto();
                    dto.setItemId((long) cartItem.getItem().getItemId());
                    return dto;
                }).collect(Collectors.toList());
    }

    @Transactional
    public void removeFromCart(String userId, int itemId) {
        cartItemRepository.deleteByUserIdAndItem_ItemId(userId, itemId);
    }
}
