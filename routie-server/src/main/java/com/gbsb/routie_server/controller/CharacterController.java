package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.CharacterStyleRequestDto;
import com.gbsb.routie_server.dto.CharacterStyleResponseDto;
import com.gbsb.routie_server.dto.EquipItemDto;
import com.gbsb.routie_server.entity.CharacterStyle;
import com.gbsb.routie_server.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/character")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    // 최초 생성 시 기본 캐릭터 생성 및 저장
    @PostMapping("/{userId}")
    public ResponseEntity<CharacterStyleResponseDto> saveOrUpdateStyle(
            @PathVariable String userId,
            @RequestBody CharacterStyleRequestDto dto) {
        CharacterStyle saved = characterService.saveOrUpdateStyle(userId, dto);
        return ResponseEntity.ok(new CharacterStyleResponseDto(saved));
    }

    // 캐릭터 상태 조회
    @GetMapping("/{userId}")
    public ResponseEntity<CharacterStyleResponseDto> getStyle(@PathVariable String userId) {
        CharacterStyle style = characterService.getStyleByUserId(userId);
        if (style == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new CharacterStyleResponseDto(style));
    }

    // 아이템 장착
    @PostMapping("/{userId}/equip")
    public ResponseEntity<CharacterStyleResponseDto> equipItem(
            @PathVariable String userId,
            @RequestBody EquipItemDto dto
    ) {
        CharacterStyle updated = characterService.equipItem(userId, dto);
        return ResponseEntity.ok(new CharacterStyleResponseDto(updated));
    }

    // 현재 장착된 아이템 조회
    @GetMapping("/{userId}/style")
    public ResponseEntity<CharacterStyleResponseDto> getEquippedItems(@PathVariable String userId) {
        CharacterStyle style = characterService.getStyleByUserId(userId);
        if (style == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new CharacterStyleResponseDto(style));
    }
}
