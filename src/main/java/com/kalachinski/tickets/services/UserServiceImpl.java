package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.Role;
import com.kalachinski.tickets.domains.State;
import com.kalachinski.tickets.domains.User;
import com.kalachinski.tickets.exceptions.NotFoundException;
import com.kalachinski.tickets.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService{

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getAllUsers() {
        log.info("Return all Users");
        return userRepository.findAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        log.info("Return User by id: {}", id);
        return user;
    }

    @Override
    public User registrationUser(User user) {
        User newUser = userRepository.save(User.builder()
                .login(user.getLogin())
                .creationDate(LocalDateTime.now())
                .password(passwordEncoder.encode(user.getPassword()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(Stream.of(Role.USER)
                        .collect(Collectors.toList()))
                .state(State.ACTIVE)
                .build());
        log.info("New User success saved with body: {}", newUser.toString());
        return newUser;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public User saveUser(User user) {
        User newUser = userRepository.save(User.builder()
                .login(user.getLogin())
                .creationDate(LocalDateTime.now())
                .password(passwordEncoder.encode(user.getPassword()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .state(user.getState())
                .build());
        log.info("New User success saved with body: {}", newUser.toString());
        return newUser;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateUser(User user, Long id) {
        User userFromDB = userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setId(id);
                    existingUser.setLogin(user.getLogin());
                    existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                    existingUser.setFirstName(user.getFirstName());
                    existingUser.setLastName(user.getLastName());
                    existingUser.setRoles(user.getRoles());
                    existingUser.setState(user.getState());
                    return userRepository.save(existingUser);
                }).orElseThrow(NotFoundException::new);
        log.info("User success updated with body: {}", userFromDB.toString());
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("User success deleted by id: {}", id);
        } else{
            log.info("User with id: {} is absent", id);
        }
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> {
            log.info("Not found User with login: " + login);
            return new UsernameNotFoundException("User not found.");
        });
    }
}
