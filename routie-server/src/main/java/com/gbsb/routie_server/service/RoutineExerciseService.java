package com.gbsb.routie_server.service;

import com.gbsb.routie_server.entity.*;
import com.gbsb.routie_server.repository.*;
import com.gbsb.routie_server.dto.ExerciseResponseDto;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineExerciseService {
    private final RoutineExerciseRepository routineExerciseRepository;
    private final RoutineRepository routineRepository;
    private final ExerciseRepository exerciseRepository;

    // 루틴에 운동 추가
    @Transactional
    public RoutineExercise addExerciseToRoutine(Long routineId, Long exerciseId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다."));
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new IllegalArgumentException("운동을 찾을 수 없습니다."));

        RoutineExercise routineExercise = RoutineExercise.builder()
                .routine(routine)
                .exercise(exercise)
                .build();

        return routineExerciseRepository.save(routineExercise);
    }

    /*// 특정 루틴의 운동 목록 조회
    public List<RoutineExercise> getExercisesByRoutine(Long routineId) {
        return routineExerciseRepository.findByRoutine_Id(routineId);
    }*/

    public List<ExerciseResponseDto> getExercisesByRoutine(Long routineId) {
        return routineExerciseRepository.findByRoutine_Id(routineId).stream()
                .map(re -> new ExerciseResponseDto(
                        re.getId(),
                        re.getExercise().getName(),
                        re.getExercise().getEnName()
                ))
                .collect(Collectors.toList());
    }


    // 루틴 내 특정 운동 삭제
    @Transactional
    public void removeExerciseFromRoutine(Long routineExerciseId) {
        RoutineExercise routineExercise = routineExerciseRepository.findById(routineExerciseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 운동을 찾을 수 없습니다."));
        Routine routine = routineExercise.getRoutine();
        routine.getExercises().remove(routineExercise);
        routineExerciseRepository.delete(routineExercise);
        routineExerciseRepository.flush();
    }
}