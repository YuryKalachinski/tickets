package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUserById(Long id);

    User saveUser(User user);

    User registrationUser(User user);

    void updateUser(User user, Long id);

    void deleteUserById(Long id);

    User getUserByLogin(String login);
}
