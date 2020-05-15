package com.kalachinski.tickets.domains;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "Ticket")
@Table(name = "ticket")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"id", "event", "location", "user", "place", "row", "ticketStatus"})
@EqualsAndHashCode(of = {"id", "place",  "row"})
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen_ticket")
    @SequenceGenerator(name = "seq_gen_ticket", sequenceName = "seq_ticket", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event.id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location.id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user.id")
    private User user;

    @Column(name = "place")
    private Integer place;

    @Column(name = "row")
    private Integer row;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket.status")
    private TicketStatus ticketStatus;
}
