package com.gbsb.routie_server.service;

import com.gbsb.routie_server.entity.Routine;
import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.entity.Exercise;
import com.gbsb.routie_server.entity.RoutineExercise;
import com.gbsb.routie_server.repository.RoutineRepository;
import com.gbsb.routie_server.repository.UserRepository;
import com.gbsb.routie_server.repository.ExerciseRepository;
import com.gbsb.routie_server.repository.RoutineExerciseRepository;
import com.gbsb.routie_server.dto.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
        newRoutine.setDays(routineRequestDto.getDays());

        Routine savedRoutine = routineRepository.save(newRoutine);

        if (routineRequestDto.getExercises() == null || routineRequestDto.getExercises().isEmpty()) {
            throw new IllegalArgumentException("운동이 포함되지 않은 루틴은 저장할 수 없습니다.");
        }

        for (ExerciseRequestDto exerciseRequest : routineRequestDto.getExercises()) {
            Exercise exercise = exerciseRepository.findById(exerciseRequest.getExerciseId())
                    .orElseThrow(() -> new IllegalArgumentException("운동을 찾을 수 없습니다."));

            RoutineExercise routineExercise = RoutineExercise.builder()
                    .routine(savedRoutine)
                    .exercise(exercise)
                    .build();

            routineExerciseRepository.save(routineExercise);
            savedRoutine.getExercises().add(routineExercise);
        }

        if (routineRequestDto.getScheduledDate() != null) {
            newRoutine.setScheduledDate(routineRequestDto.getScheduledDate());
        } else {
            newRoutine.setScheduledDate(LocalDate.now());
        }

        return savedRoutine;
    }

    // 특정 사용자의 루틴 목록 조회
    public List<Routine> getUserRoutines(String userId) {
        return routineRepository.findByUser_UserId(userId);
    }

    // 특정 날짜 루틴 조회
    public List<Routine> getRoutinesByDate(String userId, LocalDate date) {
        return routineRepository.findByUserUserIdAndScheduledDate(userId, date);
    }

    // 운동 루틴 수정
    @Transactional
    public Routine updateRoutine(Long routineId, RoutineUpdateRequestDto dto) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다."));

        routine.setName(dto.getName());
        routine.setDescription(dto.getDescription());

        // 요일 수정 로직 추가
        routine.getDays().clear();  // 기존 요일 제거
        if (dto.getDays() != null && !dto.getDays().isEmpty()) {
            routine.getDays().addAll(dto.getDays());
        }

        return routineRepository.save(routine);
    }

    public Routine getRoutine(Long routineId) {
        return routineRepository.findById(routineId).orElse(null);
    }


    // 운동 루틴 삭제
    @Transactional
    public void deleteRoutine(Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다."));

        routineRepository.delete(routine);
    }
}