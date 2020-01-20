package com.kalachinski.tickets.domains;

import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="events")
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "dateTime")
    private ZonedDateTime dateTime;

    @OneToMany
    @Column(name = "Ticket.id")
    private List<Ticket> tickets = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Location.id")
    private Location location;
}
