package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.RankingResponseDto;
import com.gbsb.routie_server.dto.RankingResultDto;
import com.gbsb.routie_server.dto.UserRankingProjection;
import com.gbsb.routie_server.repository.RoutineLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final RoutineLogRepository routineLogRepository;

    public RankingResultDto getRanking(String userId, String type) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime end = today.with(DayOfWeek.SUNDAY).atTime(23, 59, 59);

        List<UserRankingProjection> results;

        if ("calories".equals(type)) {
            results = routineLogRepository.getWeeklyCalorieRanking(start, end);
        } else {
            throw new IllegalArgumentException("지원하지 않는 타입입니다.");
        }

        List<RankingResponseDto> top10 = new ArrayList<>();
        int rank = 1;
        int myRank = -1;

        for (UserRankingProjection p : results) {
            if (p.getUserId().equals(userId)) {
                myRank = rank;
            }

            if (rank <= 10) {
                top10.add(RankingResponseDto.builder()
                        .rank(rank)
                        .nickname(p.getNickname())
                        .profileImageUrl(p.getProfileImageUrl())
                        .value(p.getTotal())
                        .build());
            }

            rank++;
        }

        return RankingResultDto.builder()
                .myRank(myRank)
                .ranking(top10)
                .build();
    }
}

