package com.kalachinski.tickets.domains;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "Event")
@Table(name="event")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id", "name", "dateTime", "location"})
@EqualsAndHashCode(of = {"id"})
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen_event")
    @SequenceGenerator(name = "seq_gen_event", sequenceName = "seq_event", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "dateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;

//    @Column(name = "createdDateTime")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
//    private LocalDateTime createdDateTime;

//    @Column(name = "updatedDateTime")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
//    private LocalDateTime updatedDateTime;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userThatUpdated", referencedColumnName = "id")
//    private User userThatUpdated;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userThatCreated", referencedColumnName = "id")
//    private User userThatCreated;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    @Column(name = "ticketId")
    private List<Ticket> tickets;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationId", referencedColumnName = "id")
    private Location location;
}
