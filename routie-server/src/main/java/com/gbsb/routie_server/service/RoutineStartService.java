package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.RoutineLogStartRequestDto;
import com.gbsb.routie_server.entity.Routine;
import com.gbsb.routie_server.entity.RoutineLog;
import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.repository.RoutineLogRepository;
import com.gbsb.routie_server.repository.RoutineRepository;
import com.gbsb.routie_server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RoutineStartService {
    private final RoutineRepository routineRepo;
    private final RoutineLogRepository routineLogRepo;
    private final UserRepository userRepo;

    public RoutineStartService(RoutineRepository routineRepo,
                               RoutineLogRepository routineLogRepo,
                               UserRepository userRepo) {
        this.routineRepo = routineRepo;
        this.routineLogRepo = routineLogRepo;
        this.userRepo = userRepo;
    }

    public Long startRoutine(RoutineLogStartRequestDto dto) {
        Routine routine = routineRepo.findById(dto.getRoutineId())
                .orElseThrow(() -> new RuntimeException("루틴을 찾을 수 없습니다"));

        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        RoutineLog log = RoutineLog.builder()
                .routine(routine)
                .user(user)
                .completionDate(LocalDateTime.now())  // 시작 시각
                .totalCaloriesBurned(0.0)
                .totalDuration(0)
                .routineSnapshot(null) // 운동 완료 전이라 비워둠
                .build();

        return routineLogRepo.save(log).getId();
    }
}
