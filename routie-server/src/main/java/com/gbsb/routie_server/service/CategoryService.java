package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.CategoryDto;
import com.gbsb.routie_server.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> {
                    CategoryDto dto = new CategoryDto();
                    dto.setCategoryId(category.getCategoryId());
                    dto.setName(category.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
