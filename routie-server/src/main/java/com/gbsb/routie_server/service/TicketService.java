package com.gbsb.routie_server.service;

import com.gbsb.routie_server.entity.TicketLog;
import com.gbsb.routie_server.entity.User;
import com.gbsb.routie_server.repository.TicketLogRepository;
import com.gbsb.routie_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketLogRepository ticketLogRepository;
    private final UserRepository userRepository;

    public int getTicketCount(String userId) {
        return ticketLogRepository.getTicketCountByUserId(userId);
    }

    public void useTicket(String userId, int amountToUse) {
        int current = getTicketCount(userId);
        if (current < amountToUse) {
            throw new IllegalStateException("티켓이 부족합니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        TicketLog log = new TicketLog(user, -amountToUse, LocalDateTime.now(), "가챠 사용");
        ticketLogRepository.save(log);
    }
}
