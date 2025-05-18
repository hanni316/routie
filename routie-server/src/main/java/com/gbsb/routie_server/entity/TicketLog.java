package com.gbsb.routie_server.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TicketLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private int amount;
    private LocalDateTime grantedAt;
    private String reason;

    public TicketLog() {}

    public TicketLog(User user, int amount, LocalDateTime grantedAt, String reason) {
        this.user = user;
        this.amount = amount;
        this.grantedAt = grantedAt;
        this.reason = reason;
    }
}
