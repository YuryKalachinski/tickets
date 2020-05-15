package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.User;

import java.util.Optional;

public interface UserService {
    Iterable<User> getAllUsers();

    User getUserById(Long id);

    User saveUser(User user);

    User registrationUser(User user);

    void updateUser(User user, Long id);

    void deleteUserById(Long id);

    Optional<User> getUserByLogin(String login);
}
