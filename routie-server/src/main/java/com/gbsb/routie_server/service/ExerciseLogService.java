package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.WorkoutRecordDto;
import com.gbsb.routie_server.entity.Exercise;
import com.gbsb.routie_server.entity.ExerciseLog;
import com.gbsb.routie_server.entity.Routine;
import com.gbsb.routie_server.entity.RoutineLog;
import org.springframework.stereotype.Service;
import com.gbsb.routie_server.repository.*;

@Service
public class ExerciseLogService {

    private final ExerciseLogRepository logRepo;
    private final RoutineLogRepository routineLogRepo;
    private final ExerciseRepository exerciseRepo;

    public ExerciseLogService(
            ExerciseLogRepository logRepo,
            RoutineLogRepository routineLogRepo,
            ExerciseRepository exerciseRepo
    ) {
        this.logRepo = logRepo;
        this.routineLogRepo = routineLogRepo;
        this.exerciseRepo = exerciseRepo;
    }

    public void saveWorkoutRecord(WorkoutRecordDto dto) {
        RoutineLog routineLog = routineLogRepo.findById(dto.getRoutineLogId())
                .orElseThrow(() -> new RuntimeException("루틴 로그를 찾을 수 없습니다."));

        Exercise exercise = exerciseRepo.findById(dto.getExerciseId())
                .orElseThrow(() -> new RuntimeException("운동을 찾을 수 없습니다."));

        double calories = exercise.getCaloriesPerSecond() * dto.getDuration();

        ExerciseLog log = ExerciseLog.builder()
                .routineLog(routineLog)
                .exercise(exercise)
                .duration(dto.getDuration())
                .caloriesBurned(calories)
                .build();

        logRepo.save(log);
    }
}
