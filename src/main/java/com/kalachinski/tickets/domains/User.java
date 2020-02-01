package com.kalachinski.tickets.domains;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usr")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"id", "login", "firstName", "lastName"})
@EqualsAndHashCode(of = {"id", "login"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
}
