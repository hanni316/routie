package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.*;
import com.gbsb.routie_server.entity.StepRecord;
import com.gbsb.routie_server.entity.StepSession;
import com.gbsb.routie_server.repository.StepRecordRepository;
import com.gbsb.routie_server.repository.StepSessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WalkSessionService {
    private final StepSessionRepository stepSessionRepository;
    private final StepRecordRepository stepRecordRepository;

    public WalkSessionService(
            StepSessionRepository stepSessionRepository,
            StepRecordRepository stepRecordRepository
    ) {
        this.stepSessionRepository = stepSessionRepository;
        this.stepRecordRepository = stepRecordRepository;
    }

    // 세션 시작: baseline 저장 후 sessionId 반환
    public WalkSessionStartResponseDto startSession(WalkSessionStartRequestDto dto) {

        StepSession session = StepSession.builder()
                .userId(dto.getUserId())
                .exerciseId(dto.getExerciseId())
                .startStepCount(dto.getStartStepCount())
                .startedAt(LocalDateTime.now())
                .build();

        StepSession saved = stepSessionRepository.save(session);

        return new WalkSessionStartResponseDto(saved.getSessionId());
    }

    // 세션 종료: delta 계산 + StepRecord 저장 + delta 응답
    public WalkSessionEndResponseDto endSession(WalkSessionEndRequestDto dto) {

        // 1. 세션 찾기
        StepSession session = stepSessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new RuntimeException("세션을 찾을 수 없습니다. sessionId=" + dto.getSessionId()));

        // 2. 걸음수 차이 계산
        int start = session.getStartStepCount() != null ? session.getStartStepCount() : 0;
        int end = dto.getEndStepCount() != null ? dto.getEndStepCount() : 0;
        int deltaSteps = Math.max(end - start, 0); // 음수 방어

        // 3. StepRecord로 확정 저장
        StepRecord record = StepRecord.builder()
                .userId(session.getUserId())
                .exerciseId(session.getExerciseId())
                .stepsDuringSession(deltaSteps)
                .durationMillis(dto.getDurationMillis())
                .finishedAt(LocalDateTime.now())
                .build();

        stepRecordRepository.save(record);

        // (선택) 세션 StepSession은 남겨둘 수도 있고, 정리해도 되고
        // 여기서는 그냥 남겨둠 (디버깅/이력 확인용)

        // 4. 워치에 돌려줄 응답
        return new WalkSessionEndResponseDto(
                deltaSteps,
                dto.getDurationMillis()
        );
    }
}
