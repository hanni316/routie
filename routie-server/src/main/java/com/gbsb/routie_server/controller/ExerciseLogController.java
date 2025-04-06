package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.WorkoutRecordDto;
import com.gbsb.routie_server.entity.ExerciseLog;
import com.gbsb.routie_server.service.ExerciseLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exercise-logs")
public class ExerciseLogController {

    private final ExerciseLogService exerciseLogService;

    public ExerciseLogController(ExerciseLogService exerciseLogService) {
        this.exerciseLogService = exerciseLogService;
    }

    @PostMapping
    public ResponseEntity<Void> saveLog(@RequestBody WorkoutRecordDto dto) {
        exerciseLogService.saveWorkoutRecord(dto);
        return ResponseEntity.ok().build();
    }
}
