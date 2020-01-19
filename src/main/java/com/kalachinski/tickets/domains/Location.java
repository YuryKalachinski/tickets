package com.kalachinski.tickets.domains;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "locations")
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "quantityPlace")
    private Integer quantityPlace;

    @Column(name = "quantityRow")
    private Integer quantityRow;

    @Column(name = "quantitySector")
    private Integer quantitySector;

    @OneToMany
    @Column(name = "Event.id")
    private List<Event> events;

    @OneToMany
    @Column(name = "Ticket.id")
    private List<Ticket> tickets;

    public Long getId() {
        return id;
    }
}