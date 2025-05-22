package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.TicketCountResponseDto;
import com.gbsb.routie_server.dto.TicketUseRequestDto;
import com.gbsb.routie_server.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/{userId}/count")
    public ResponseEntity<TicketCountResponseDto> getTicketCount(@PathVariable String userId) {
        int count = ticketService.getTicketCount(userId);
        return ResponseEntity.ok(new TicketCountResponseDto(count));
    }

    @PostMapping("/use")
    public ResponseEntity<Void> useTicket(@RequestBody TicketUseRequestDto dto) {
        ticketService.useTicket(dto.getUserId(), dto.getAmount());
        return ResponseEntity.ok().build();
    }
}
