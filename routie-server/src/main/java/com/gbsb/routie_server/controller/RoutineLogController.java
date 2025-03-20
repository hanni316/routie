package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.RoutineLogDto;
import com.gbsb.routie_server.dto.RoutineLogRequestDto;
import com.gbsb.routie_server.entity.RoutineLog;
import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.service.RoutineLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routines")
public class RoutineLogController {

    private final RoutineLogService routineLogService;

    public RoutineLogController(RoutineLogService routineLogService) {
        this.routineLogService = routineLogService;
    }

    @PostMapping("/complete")
    public ResponseEntity<RoutineLogDto> completeRoutine(
            @RequestBody RoutineLogRequestDto requestDto,
            @AuthenticationPrincipal User user) {

        RoutineLog routineLog = routineLogService.completeRoutine(requestDto, user);
        RoutineLogDto dto = new RoutineLogDto(routineLog);
        return ResponseEntity.ok(dto);
    }
}
