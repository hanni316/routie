package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.*;
import com.gbsb.routie_server.service.WalkSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/walk-sessions")
public class WalkSessionController {

    private final WalkSessionService walkSessionService;

    public WalkSessionController(WalkSessionService walkSessionService) {
        this.walkSessionService = walkSessionService;
    }

    // 세션 시작
    @PostMapping("/start")
    public ResponseEntity<WalkSessionStartResponseDto> startSession(
            @RequestBody WalkSessionStartRequestDto dto
    ) {
        WalkSessionStartResponseDto response = walkSessionService.startSession(dto);
        return ResponseEntity.ok(response);
    }

    // 세션 종료
    @PostMapping("/end")
    public ResponseEntity<WalkSessionEndResponseDto> endSession(
            @RequestBody WalkSessionEndRequestDto dto
    ) {
        WalkSessionEndResponseDto response = walkSessionService.endSession(dto);
        return ResponseEntity.ok(response);
    }
}
