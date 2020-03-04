package com.kalachinski.tickets.controllers;

import com.kalachinski.tickets.domains.Role;
import com.kalachinski.tickets.domains.State;
import com.kalachinski.tickets.domains.User;
import com.kalachinski.tickets.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/registry")
public class RegistrationRestController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationRestController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<User> saveAsUser(@RequestBody User user) {
        if (user != null && !userService.getUserByUsername(user.getLogin()).isPresent()) {
            List<Role> roles = new ArrayList<>();
            roles.add(Role.USER);
            return new ResponseEntity<>(userService.saveUser(
                    User.builder()
                            .login(user.getLogin())
                            .creationDate(LocalDateTime.now())
                            .password(passwordEncoder.encode(user.getPassword()))
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .roles(roles)
                            .state(State.ACTIVE)
                            .build()),
                    HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
