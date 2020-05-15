package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.Role;
import com.kalachinski.tickets.domains.State;
import com.kalachinski.tickets.domains.User;
import com.kalachinski.tickets.exceptions.BadRequestException;
import com.kalachinski.tickets.exceptions.NotFoundException;
import com.kalachinski.tickets.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService{

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Iterable<User> getAllUsers() {
        log.info("Return all Users");
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        log.info("Return User by id: {}", id);
        return user;
    }

    @Override
    public User registrationUser(User user) {
        if (user.getLogin() == null || user.getFirstName() == null || user.getLastName() == null
                || user.getPassword() == null) {
            throw new BadRequestException();
        }
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
    public User saveUser(User user) {
        if (user.getLogin() == null || user.getFirstName() == null || user.getLastName() == null
                || user.getPassword() == null) {
            throw new BadRequestException();
        }
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
    public void updateUser(User user, Long id) {
        Optional.ofNullable(user).orElseThrow(BadRequestException::new);
        if (user.getLogin() == null || user.getFirstName() == null || user.getLastName() == null
                || user.getPassword() == null) {
            throw new BadRequestException();
        }
        if (!id.equals(user.getId())) {
            user.setId(id);
        }
        User updateUser = userRepository.save(User.builder()
                .id(user.getId())
                .login(user.getLogin())
                .creationDate(LocalDateTime.now())
                .password(passwordEncoder.encode(user.getPassword()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .state(user.getState())
                .build());
        log.info("User success updated with body: {}", updateUser.toString());
    }

    @Override
    public void deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("User success deleted by id: {}", id);
        } else{
            log.info("User with id: {} is absent", id);
        }
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
