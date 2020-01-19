package com.kalachinski.tickets.domains;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="tickets")
@Data
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Event.id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Location.id")
    private Location location;

    @Column(name = "place")
    private Integer place;

    @Column(name = "row")
    private Integer row;

    @Column(name = "sector")
    private String sector;

    @Enumerated(EnumType.STRING)
    @Column(name = "Ticket.status")
    private TicketStatus ticketStatus;

    public Long getId() {
        return id;
    }
}
