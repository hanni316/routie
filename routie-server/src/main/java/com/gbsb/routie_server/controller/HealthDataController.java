package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.HealthDataDto;
import com.gbsb.routie_server.service.HealthDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthDataController {
    private final HealthDataService service;

    @PostMapping
    public ResponseEntity<String> upload(@RequestBody HealthDataDto dto) {
        service.save(dto);
        return ResponseEntity.ok("헬스 데이터 저장 완료!");
        //후에 dto 값을 반환하고 싶다면 <HealthDataDto> 변경하면 됩니닷!
    }
}
