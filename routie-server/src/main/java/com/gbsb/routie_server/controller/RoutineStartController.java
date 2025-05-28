package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.RoutineLogStartRequestDto;
import com.gbsb.routie_server.service.RoutineStartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routine-logs")
public class RoutineStartController {
    private final RoutineStartService routineStartService;

    public RoutineStartController(RoutineStartService routineStartService) {
        this.routineStartService = routineStartService;
    }

    @PostMapping("/start")
    public ResponseEntity<Long> startRoutine(@RequestBody RoutineLogStartRequestDto dto) {
        Long routineLogId = routineStartService.startRoutine(dto);
        return ResponseEntity.ok(routineLogId);
    }
}
