package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.CharacterStyleRequestDto;
import com.gbsb.routie_server.dto.EquipItemDto;
import com.gbsb.routie_server.entity.CharacterStyle;
import com.gbsb.routie_server.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;

    public CharacterStyle saveOrUpdateStyle(String userId, CharacterStyleRequestDto dto) {
        return characterRepository.findByUserId(userId)
                .map(existing -> {
                    existing.setHair(dto.getHair());
                    existing.setOutfit(dto.getOutfit());
                    existing.setBottom(dto.getBottom());
                    existing.setAccessory(dto.getAccessory());
                    existing.setShoes(dto.getShoes());
                    existing.setBackground(dto.getBackground());
                    return characterRepository.save(existing);
                })
                .orElseGet(() -> {
                    CharacterStyle newStyle = CharacterStyle.builder()
                            .userId(userId)
                            .hair(dto.getHair())
                            .outfit(dto.getOutfit())
                            .bottom(dto.getBottom())
                            .accessory(dto.getAccessory())
                            .shoes(dto.getShoes())
                            .background(dto.getBackground())
                            .build();
                    return characterRepository.save(newStyle);
                });
    }

    public CharacterStyle getStyleByUserId(String userId) {
        return characterRepository.findByUserId(userId)
                .orElse(null);
    }

    public CharacterStyle equipItem(String userId, EquipItemDto dto) {
        CharacterStyle style = characterRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터 스타일을 찾을 수 없습니다."));

        if (style == null) {
            // 없으면 새로 생성
            style = CharacterStyle.builder()
                    .userId(userId)
                    .hair(null)
                    .outfit(null)
                    .bottom(null)
                    .accessory(null)
                    .shoes(null)
                    .background(null)
                    .build();
        }

        switch (dto.getCategoryName()) {
            case "헤어":
                style.setHair(dto.getItemName());
                break;
            case "상의":
                style.setOutfit(dto.getItemName());
                break;
            case "하의":
                style.setBottom(dto.getItemName());
                break;
            case "악세서리":
                style.setAccessory(dto.getItemName());
                break;
            case "신발":
                style.setShoes(dto.getItemName());
                break;
            case "배경":
                style.setBackground(dto.getItemName());
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + dto.getCategoryName());
        }

        return characterRepository.save(style);
    }
}
