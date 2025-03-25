package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.*;
import com.gbsb.routie_server.entity.*;
import com.gbsb.routie_server.repository.RoutineLogRepository;
import com.gbsb.routie_server.repository.RoutineRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoutineLogService {
    private final RoutineRepository routineRepository;
    private final RoutineLogRepository routineLogRepository;
    private final ObjectMapper objectMapper;

    public RoutineLogService(RoutineRepository routineRepository,
                             RoutineLogRepository routineLogRepository,
                             ObjectMapper objectMapper) {
        this.routineRepository = routineRepository;
        this.routineLogRepository = routineLogRepository;
        this.objectMapper = objectMapper;
    }

    public List<RoutineLog> getLogsByDate(String userId, LocalDate date) {
        return routineLogRepository.findByUserIdAndDate(userId, date);
    }



    public RoutineLog completeRoutine(RoutineLogRequestDto requestDto, User user) {
        Routine routine = routineRepository.findById(requestDto.getRoutineId())
                .orElseThrow(() -> new RuntimeException("Routine not found"));

        List<RoutineExercise> routineExercises = routine.getExercises();
        Map<Long, Integer> actualDurationMap = requestDto.getExercises().stream()
                .collect(Collectors.toMap(ExerciseLogRequestDto::getExerciseId, ExerciseLogRequestDto::getDuration));

        int totalDuration = 0;
        double totalCalories = 0;
        List<ExerciseLog> exerciseLogs = new ArrayList<>();

        for (RoutineExercise re : routineExercises) {
            int actualDuration = actualDurationMap.getOrDefault(re.getExercise().getId(), 0);
            double actualCalories = re.getExercise().getCaloriesPerSecond() * actualDuration;

            totalDuration += actualDuration;
            totalCalories += actualCalories;

            ExerciseLog exerciseLog = ExerciseLog.builder()
                    .exercise(re.getExercise())
                    .duration(actualDuration)
                    .caloriesBurned(actualCalories)
                    .build();
            exerciseLogs.add(exerciseLog);
        }

        RoutineLog routineLog = RoutineLog.builder()
                .routine(routine)
                .user(user)
                .completionDate(LocalDateTime.now())
                .totalDuration(totalDuration)
                .totalCaloriesBurned(totalCalories)
                .build();

        exerciseLogs.forEach(el -> el.setRoutineLog(routineLog));
        routineLog.setExerciseLogs(exerciseLogs);

        List<Map<String, Object>> snapshotList = new ArrayList<>();
        for (ExerciseLog el : exerciseLogs) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("exerciseName", el.getExercise().getName());
            entry.put("duration", el.getDuration());
            entry.put("caloriesBurned", el.getCaloriesBurned());
            snapshotList.add(entry);
        }

        try {
            routineLog.setRoutineSnapshot(objectMapper.writeValueAsString(snapshotList));
        } catch (JsonProcessingException e) {
            log.error("루틴 스냅샷 직렬화 실패", e);
        }

        return routineLogRepository.save(routineLog);
    }
}
