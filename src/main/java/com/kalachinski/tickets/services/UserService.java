package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);

    void deleteUserById(Long id);

    Optional<User> getUserById(Long id);

    Optional<User> getUserByUsername(String username);

    List<User> getAllUsers();
}
