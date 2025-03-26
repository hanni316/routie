package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.RoutineLogDto;
import com.gbsb.routie_server.dto.RoutineLogRequestDto;
import com.gbsb.routie_server.entity.RoutineLog;
import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.service.RoutineLogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/logs")
    public ResponseEntity<List<RoutineLogDto>> getLogsByDate(
            @RequestParam String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<RoutineLog> logs = routineLogService.getLogsByDate(userId, date);
        List<RoutineLogDto> dtos = logs.stream().map(RoutineLogDto::new).toList();
        return ResponseEntity.ok(dtos);
    }

}
