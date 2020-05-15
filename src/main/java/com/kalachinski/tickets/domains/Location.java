package com.kalachinski.tickets.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "Location")
@Table(name = "location")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id", "name", "numberOfPlace", "numberOfRow"})
@EqualsAndHashCode(of = {"id"})
public class Location implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen_location")
    @SequenceGenerator(name = "seq_gen_location", sequenceName = "seq_location", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "numberOfPlace")
    private Integer numberOfPlace;

    @Column(name = "numberOfRow")
    private Integer numberOfRow;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    @Column(name = "event.id")
    @JsonIgnore
    private List<Event> events;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    @Column(name = "ticket.id")
    @JsonIgnore
    private List<Ticket> tickets;
}