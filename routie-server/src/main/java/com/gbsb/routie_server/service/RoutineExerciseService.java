package com.gbsb.routie_server.service;

import com.gbsb.routie_server.entity.*;
import com.gbsb.routie_server.repository.*;
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
    public RoutineExercise addExerciseToRoutine(Long routineId, Long exerciseId, int duration) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다."));
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new IllegalArgumentException("운동을 찾을 수 없습니다."));

        //User user = routine.getUser();

        double caloriesBurned = (exercise.getCaloriesPerSecond() * duration * 60);

        RoutineExercise routineExercise = RoutineExercise.builder()
                .routine(routine)
                .exercise(exercise)
                .duration(duration)
                .caloriesBurned(caloriesBurned)
                .build();


        routineExerciseRepository.save(routineExercise);
        //updateRoutineStats(routineId);
        //routine.updateRoutineStats();
        routineRepository.save(routine);

        return routineExercise;
    }

    /*@Transactional (실행 안됨.. 일단 주석 처리)
    public void updateRoutineStats(Long routineId) {
        // 1. 루틴 엔티티를 조회
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다."));

        // 2. 해당 루틴에 속한 모든 RoutineExercise 데이터를 DB에서 가져옴
        List<RoutineExercise> exerciseList = routineExerciseRepository.findByRoutine_Id(routineId);

        // 3. 총 운동 시간과 총 칼로리를 합산
        int totalDuration = 0;
        double totalCalories = 0.0;
        for (RoutineExercise re : exerciseList) {
            totalDuration += re.getDuration();
            totalCalories += re.getCaloriesBurned();
        }

        // 4. 합산한 값을 루틴 엔티티에 설정
        routine.setDuration(totalDuration);
        routine.setCaloriesBurned(totalCalories);

        // 5. 변경된 루틴 엔티티를 저장
        routineRepository.save(routine);
    }*/

    // 특정 루틴의 운동 목록 조회
    public List<RoutineExercise> getExercisesByRoutine(Long routineId) {
        return routineExerciseRepository.findByRoutine_Id(routineId);
    }

    // 루틴 내 특정 운동 삭제
    @Transactional
    public void removeExerciseFromRoutine(Long routineExerciseId) {
        RoutineExercise routineExercise = routineExerciseRepository.findById(routineExerciseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 운동을 찾을 수 없습니다."));
        routineExerciseRepository.delete(routineExercise);
    }
    /* 특정 루틴운동 ID로 조회(아직 미사용)
    public RoutineExercise getRoutineExerciseById(Long routineExerciseId) {
        return routineExerciseRepository.findById(routineExerciseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 루틴운동을 찾을 수 없습니다."));
    }

    // 변경된 루틴운동을 저장
    @Transactional
    public RoutineExercise saveRoutineExercise(RoutineExercise routineExercise) {
        return routineExerciseRepository.save(routineExercise);
    }*/
}