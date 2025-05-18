package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.UserRankingProjection;
import com.gbsb.routie_server.entity.TicketLog;
import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.repository.RoutineLogRepository;
import com.gbsb.routie_server.repository.TicketLogRepository;
import com.gbsb.routie_server.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RankingRewardService {

    private final RoutineLogRepository routineLogRepository;
    private final UserRepository userRepository;
    private final TicketLogRepository ticketLogRepository;

    public RankingRewardService(RoutineLogRepository routineLogRepository,
                                UserRepository userRepository,
                                TicketLogRepository ticketLogRepository) {
        this.routineLogRepository = routineLogRepository;
        this.userRepository = userRepository;
        this.ticketLogRepository = ticketLogRepository;
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * MON") // 매주 월요일 00시
    public void distributeRankingRewards() {
        LocalDateTime end = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0); // 이번주 월요일 00:00
        LocalDateTime start = end.minusWeeks(1); // 지난주 월요일 00:00

        List<UserRankingProjection> rankingList = routineLogRepository.getWeeklyCalorieRanking(start, end);

        for (int i = 0; i < Math.min(10, rankingList.size()); i++) {
            UserRankingProjection ranker = rankingList.get(i);

            int base = 1;
            int bonus = switch (i) {
                case 0 -> 5;  // 1위
                case 1 -> 3;  // 2위
                case 2 -> 1;  // 3위
                default -> 0;
            };
            int total = base + bonus;

            User user = userRepository.findById(ranker.getUserId()).orElse(null);
            if (user == null) {
                System.err.println("유저 없음: " + ranker.getUserId());
                continue;
            }

            user.addTickets(total);

            TicketLog log = new TicketLog(
                    user,
                    total,
                    LocalDateTime.now(),
                    "Ranking reward"  // 보상 사유 기록
            );
            ticketLogRepository.save(log);
        }
    }
}