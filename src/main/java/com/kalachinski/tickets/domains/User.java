package com.kalachinski.tickets.domains;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "User")
@Table(name = "usr")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"id", "login", "firstName", "lastName", "state", "roles"})
@EqualsAndHashCode(of = {"id"})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen_user")
    @SequenceGenerator(name = "seq_gen_user", sequenceName = "seq_user", allocationSize = 1)
    @JsonView(Views.Id.class)
    private Long id;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "userRole", joinColumns = @JoinColumn(name = "userId"))
    @Enumerated(EnumType.STRING)
    @JsonView(Views.FullViews.class)
    private List<Role> roles;

    @Column(name = "creationDate", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonView(Views.FullViews.class)
    private LocalDateTime creationDate;

    @Column(name = "login")
    @JsonView(Views.IdName.class)
    private String login;

    @Column(name = "password")
    @JsonView(Views.IdName.class)
    private String password;

    @Transient
    private String confirmPassword;

    @Column(name = "firstName")
    @JsonView(Views.IdName.class)
    private String firstName;

    @Column(name = "lastName")
    @JsonView(Views.IdName.class)
    private String lastName;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    @JsonView(Views.FullViews.class)
    private State state;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Column(name = "ticket.id")
    @JsonIgnore
    private List<Ticket> tickets;
}
