package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.ItemDto;
import com.gbsb.routie_server.entity.Item;
import com.gbsb.routie_server.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    // 전체 아이템을 DTO로 조회
    public List<ItemDto> getAllItemDtos() {
        return itemRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 카테고리별 아이템을 DTO로 조회
    public List<ItemDto> getItemDtosByCategory(int categoryId) {
        return itemRepository.findByCategory_CategoryId(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 변환 로직 따로 분리 (Item → ItemDto)
    private ItemDto convertToDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setItemId(item.getItemId());
        dto.setName(item.getName());
        dto.setNameEn(item.getNameEn());
        dto.setPrice(item.getPrice());
        dto.setCategoryName(item.getCategory().getName());
        return dto;
    }
}