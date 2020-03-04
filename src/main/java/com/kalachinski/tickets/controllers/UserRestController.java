package com.kalachinski.tickets.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.kalachinski.tickets.domains.Role;
import com.kalachinski.tickets.domains.State;
import com.kalachinski.tickets.domains.User;
import com.kalachinski.tickets.domains.Views;
import com.kalachinski.tickets.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserRestController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRestController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @JsonView(Views.FullViews.class)
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @JsonView(Views.FullViews.class)
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @GetMapping("/{login}")
//    public ResponseEntity<User> getUserByLogin(@PathVariable String login) {
//        return userService.getUserByUsername(login)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable Long id) {
        if (userService.getUserById(id).isPresent()) {
            userService.deleteUserById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long id) {
        Optional<User> userFromDb = userService.getUserById(id);
        if (userFromDb.isPresent()
                && Optional.ofNullable(user).isPresent()
                && id.equals(user.getId())
                && userFromDb.get().getLogin().equals(user.getLogin())
                ) {
            userFromDb.get().setLogin(user.getLogin());
            userFromDb.get().setFirstName(user.getFirstName());
            userFromDb.get().setLastName(user.getLastName());
            userFromDb.get().setPassword(passwordEncoder.encode(user.getPassword()));
            userFromDb.get().setRoles(user.getRoles());
            userFromDb.get().setState(user.getState());
            return new ResponseEntity<>(userService.saveUser(userFromDb.get()), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
