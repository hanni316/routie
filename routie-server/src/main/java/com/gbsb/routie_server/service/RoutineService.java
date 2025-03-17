package com.gbsb.routie_server.service;

import com.gbsb.routie_server.entity.Routine;
import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.entity.Exercise;
import com.gbsb.routie_server.entity.RoutineExercise;
import com.gbsb.routie_server.repository.RoutineRepository;
import com.gbsb.routie_server.repository.UserRepository;
import com.gbsb.routie_server.repository.ExerciseRepository;
import com.gbsb.routie_server.repository.RoutineExerciseRepository;
import com.gbsb.routie_server.dto.RoutineRequestDto;
import com.gbsb.routie_server.dto.ExerciseRequestDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final RoutineExerciseRepository routineExerciseRepository;

    // 운동 루틴 생성
    @Transactional
    public Routine createRoutineWithExercises(String userId, RoutineRequestDto routineRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Routine newRoutine = new Routine();
        newRoutine.setName(routineRequestDto.getName());
        newRoutine.setDescription(routineRequestDto.getDescription());
        newRoutine.setUser(user);

        Routine savedRoutine = routineRepository.save(newRoutine);

        if (routineRequestDto.getExercises() != null && !routineRequestDto.getExercises().isEmpty()) {
            for (ExerciseRequestDto exerciseRequest : routineRequestDto.getExercises()) {
                Exercise exercise = exerciseRepository.findById(exerciseRequest.getExerciseId())
                        .orElseThrow(() -> new IllegalArgumentException("운동을 찾을 수 없습니다."));

                RoutineExercise routineExercise = new RoutineExercise();
                routineExercise.setRoutine(savedRoutine);  // 방금 저장된 루틴 사용
                routineExercise.setExercise(exercise);
                routineExercise.setDuration(exerciseRequest.getDuration());

                routineExerciseRepository.save(routineExercise); // 운동 저장
            }
        } else {
            throw new IllegalArgumentException("운동이 포함되지 않은 루틴은 저장할 수 없습니다.");
        }
        return routineRepository.save(newRoutine);
    }

    // 특정 사용자의 루틴 목록 조회
    public List<Routine> getUserRoutines(String userId) {
        return routineRepository.findByUser_UserId(userId);
    }

    // 운동 루틴 수정
    @Transactional
    public Routine updateRoutine(Long routineId, Routine updatedRoutine) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다."));

        routine.setName(updatedRoutine.getName());
        routine.setDescription(updatedRoutine.getDescription());

        return routineRepository.save(routine);
    }

    // 운동 루틴 삭제
    @Transactional
    public void deleteRoutine(Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다."));

        routineRepository.delete(routine);
    }
}