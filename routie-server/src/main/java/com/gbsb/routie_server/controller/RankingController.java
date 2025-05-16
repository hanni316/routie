package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.RankingResultDto;
import com.gbsb.routie_server.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RankingController {
    private final RankingService rankingService;

    @GetMapping("/ranking")
    public ResponseEntity<RankingResultDto> getRanking(
            @RequestParam String userId,
            @RequestParam(defaultValue = "calories") String type
    ) {
        return ResponseEntity.ok(rankingService.getRanking(userId, type));
    }
}

