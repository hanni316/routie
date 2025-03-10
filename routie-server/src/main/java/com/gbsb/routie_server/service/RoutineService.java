package com.gbsb.routie_server.service;

import com.gbsb.routie_server.entity.Routine;
import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.repository.RoutineRepository;
import com.gbsb.routie_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final UserRepository userRepository;

    // 운동 루틴 생성
    @Transactional
    public Routine createRoutine(String userId, Routine routine) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        routine.setUser(user);
        return routineRepository.save(routine);
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
